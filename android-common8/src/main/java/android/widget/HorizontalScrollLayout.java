package android.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint({ "ClickableViewAccessibility", "HandlerLeak" })
public class HorizontalScrollLayout extends ViewGroup {

	private VelocityTracker mVelocityTracker;
	private static final int SNAP_VELOCITY = 600;
	private Scroller mScroller;
	private int currentItem;
	private int mDefaultScreen = 0;
	private float mLastMotionX;
	private OnViewChangeListener mOnViewChangeListener;

	public HorizontalScrollLayout(Context context) {
		super(context);
		init(context);
	}

	public HorizontalScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public HorizontalScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private static final int timerAnimation = 1;
	private long time = 3500;
	private boolean autoPlay = true;
	private final Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case timerAnimation:
				if (currentItem >= getChildCount() - 1) {
					snapToScreen(0);
				} else if (currentItem < getChildCount() - 1) {
					snapToScreen(currentItem + 1);
				} else {
					snapToDestination();
				}
				break;
			default:
				break;
			}
		};
	};

	private final Timer timer = new Timer();
	private final TimerTask task = new TimerTask() {
		public void run() {
			if (autoPlay) {
				mHandler.sendEmptyMessage(timerAnimation);
			} else {
				timer.cancel();
			}
		}
	};

	private void init(Context context) {
		currentItem = mDefaultScreen;
		mScroller = new Scroller(context);
		// timer.schedule(task, time, time);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(currentItem * width, 0);
	}

	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {
			final int delta = whichScreen * getWidth() - getScrollX();
			int durntion = Math.abs(delta) / 2;
			if (whichScreen == 0) {
				durntion = 50;
			}
			mScroller.startScroll(getScrollX(), 0, delta, 0, durntion);
			currentItem = whichScreen;
			invalidate();
			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.OnViewChange(this, currentItem);
			}
		}
	}

	public void setAdapter(final BaseAdapter adapter, boolean isRemoveAllViews) {
		if (isRemoveAllViews) {
			removeAllViews();
		}
		for (int i = 0; i < adapter.getCount(); i++) {
			View view = adapter.getView(i, null, null);
			addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float x = event.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			final float dx = (int) (mLastMotionX - x);
			if (IsCanMove(dx)) {
				if (mVelocityTracker != null) {
					mVelocityTracker.addMovement(event);
				}
				mLastMotionX = x;
				scrollBy((int) dx, 0);
			}
			break;
		case MotionEvent.ACTION_UP:
			int velocityX = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityX = (int) mVelocityTracker.getXVelocity();
			}
			if (velocityX > SNAP_VELOCITY && currentItem > 0) {
				snapToScreen(currentItem - 1);
			} else if (velocityX < -SNAP_VELOCITY && currentItem < getChildCount() - 1) {
				snapToScreen(currentItem + 1);
			} else {
				snapToDestination();
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}
		return true;
	}

	private boolean IsCanMove(float deltaX) {
		if (getScrollX() <= 0 && deltaX < 0) {
			return false;
		}
		if (getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0) {
			return false;
		}
		return true;
	}

	public void SetOnViewChangeListener(OnViewChangeListener listener) {
		mOnViewChangeListener = listener;
	}

	public interface OnViewChangeListener {
		public void OnViewChange(HorizontalScrollLayout view, int position);
	}

}
