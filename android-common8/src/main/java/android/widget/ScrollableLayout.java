package android.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

@SuppressLint({ "NewApi", "Recycle" })
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class ScrollableLayout extends LinearLayout {

	@SuppressWarnings("unused")
	private String TAG = ScrollableLayout.class.getSimpleName();

	private Scroller mScroller;
	private float mDownX;
	private float mDownY;
	private float mLastY;
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;
	private int mMinimumVelocity;
	private int mMaximumVelocity;
	private DIRECTION mDirection;
	private int mHeadHeight;
	private View mHeadView;
	private int mExpandHeight = 0;
	private int sysVersion;
	private ViewGroup mViewGroup;
	private boolean flag1, flag2;
	private int mLastScrollerY;
	private boolean mDisallowIntercept;
	private boolean isSmoothTop = true;
	private int stickyOffset;

	enum DIRECTION {
		UP, DOWN
	}

	private int minY = 0;
	private int maxY = 0;

	private int mCurY;
	private boolean isClickHead;
	private boolean isClickHeadExpand;

	// public interface OnScrollListener {
	// void onScroll(int currentY, int maxY);
	// }
	//
	// private OnScrollListener onScrollListener;
	//
	// public void setOnScrollListener(OnScrollListener onScrollListener) {
	// this.onScrollListener = onScrollListener;
	// }

	private ScrollableHelper mHelper;

	public ScrollableHelper getHelper() {
		return mHelper;
	}

	public ScrollableLayout(Context context) {
		super(context);
		init(context);
	}

	public ScrollableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
	public void init(Context context) {
		mHelper = new ScrollableHelper();
		mScroller = new Scroller(context);
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		sysVersion = Build.VERSION.SDK_INT;
	}

	private float currentOffset;

	@TargetApi(Build.VERSION_CODES.DONUT)
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		float currentX = ev.getX();
		float currentY = ev.getY();
		float deltaY;
		int shiftX;
		int shiftY;
		shiftX = (int) Math.abs(currentX - mDownX);
		shiftY = (int) Math.abs(currentY - mDownY);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDisallowIntercept = false;
			flag1 = true;
			flag2 = true;
			mDownX = currentX;
			mDownY = currentY;
			mLastY = currentY;
			checkIsClickHead((int) currentY, mHeadHeight, getScrollY());
			checkIsClickHeadExpand((int) currentY, mHeadHeight, getScrollY());
			initOrResetVelocityTracker();
			mVelocityTracker.addMovement(ev);
			mScroller.forceFinished(true);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mDisallowIntercept) {
				break;
			}
			initVelocityTrackerIfNotExists();
			mVelocityTracker.addMovement(ev);
			deltaY = mLastY - currentY;
			if (flag1) {
				if (shiftX > mTouchSlop && shiftX > shiftY) {
					flag1 = false;
					flag2 = false;
				} else if (shiftY > mTouchSlop && shiftY > shiftX) {
					flag1 = false;
					flag2 = true;
				}
			}
			if (flag2 && shiftY > mTouchSlop && shiftY > shiftX && (!isSticked() || mHelper.isTop() || isClickHeadExpand)) {
				if (deltaY <= 0 && getScrollY() + deltaY <= 0) {
					isSmoothTop = true;
					requestDisallowInterceptTouchEvent(false);
				} else {
					isSmoothTop = false;
					requestDisallowInterceptTouchEvent(true);
				}
				if (mViewGroup != null && mViewGroup instanceof ViewGroup) {
					mViewGroup.requestDisallowInterceptTouchEvent(true);
				}
				scrollBy(0, (int) (deltaY + 0.5));
				if (mScrollLayoutListener != null) {
					currentOffset += (int) (deltaY + 0.5);
					currentOffset = currentOffset >= mHeadHeight ? mHeadHeight : currentOffset;
					currentOffset = currentOffset <= 0 ? 0 : currentOffset;
					mScrollLayoutListener.onScrollLayout(this, isSmoothTop, (int) currentOffset);
				}
			} else {
				requestDisallowInterceptTouchEvent(true);
				isSmoothTop = false;
			}
			mLastY = currentY;
			break;
		case MotionEvent.ACTION_UP:
			if (flag2 && shiftY > shiftX && shiftY > mTouchSlop) {
				mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				float yVelocity = -mVelocityTracker.getYVelocity();
				if (Math.abs(yVelocity) > mMinimumVelocity) {
					mDirection = yVelocity > 0 ? DIRECTION.UP : DIRECTION.DOWN;
					if (mDirection == DIRECTION.UP && isSticked()) {
					} else {
						mScroller.fling(0, getScrollY(), 0, (int) yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
						mScroller.computeScrollOffset();
						mLastScrollerY = getScrollY();
						invalidate();
					}
				}
				if (isClickHead || !isSticked()) {
					int action = ev.getAction();
					ev.setAction(MotionEvent.ACTION_CANCEL);
					boolean dd = super.dispatchTouchEvent(ev);
					ev.setAction(action);
					return dd;
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			if (flag2 && isClickHead && (shiftX > mTouchSlop || shiftY > mTouchSlop)) {
				int action = ev.getAction();
				ev.setAction(MotionEvent.ACTION_CANCEL);
				boolean dd = super.dispatchTouchEvent(ev);
				ev.setAction(action);
				return dd;
			}
			break;
		default:
			break;
		}
		super.dispatchTouchEvent(ev);
		return true;
	}

	public void requestScrollableLayoutDisallowInterceptTouchEvent(boolean disallowIntercept) {
		super.requestDisallowInterceptTouchEvent(disallowIntercept);
		mDisallowIntercept = disallowIntercept;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private int getScrollerVelocity(int distance, int duration) {
		if (mScroller == null) {
			return 0;
		} else if (sysVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return (int) mScroller.getCurrVelocity();
		} else {
			return distance / duration;
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			final int currY = mScroller.getCurrY();
			if (mDirection == DIRECTION.UP) {
				if (isSticked()) {
					int distance = mScroller.getFinalY() - currY;
					int duration = calcDuration(mScroller.getDuration(), mScroller.timePassed());
					mHelper.smoothScrollBy(getScrollerVelocity(distance, duration), distance, duration);
					mScroller.forceFinished(true);
					requestDisallowInterceptTouchEvent(true);
					if (mScrollLayoutListener != null) {
						currentOffset = mHeadHeight;
						mScrollLayoutListener.onScrollLayout(this, isSmoothTop, mHeadHeight);
					}
					return;
				} else {
					scrollTo(0, currY);
				}
			} else {
				if (mHelper.isTop() || isClickHeadExpand) {
					int deltaY = (currY - mLastScrollerY);
					int toY = getScrollY() + deltaY;
					scrollTo(0, toY);
					if (mCurY <= minY) {
						mScroller.forceFinished(true);
						isSmoothTop = true;// 滑动到顶部
						requestDisallowInterceptTouchEvent(false);
						if (mScrollLayoutListener != null) {
							currentOffset = 0;
							mScrollLayoutListener.onScrollLayout(this, isSmoothTop, (int) currentOffset);
						}
						return;
					}
				}
				invalidate();
			}
			mLastScrollerY = currY;
		}
	}

	@Override
	public void scrollBy(int x, int y) {
		int scrollY = getScrollY();
		int toY = scrollY + y;
		if (toY >= maxY) {
			toY = maxY;
		} else if (toY <= minY) {
			toY = minY;
		}
		y = toY - scrollY;
		super.scrollBy(x, y);
	}

	public boolean isSticked() {
		return mCurY == maxY;
	}

	@Override
	public void scrollTo(int x, int y) {
		if (y >= maxY) {
			y = maxY;
		} else if (y <= minY) {
			y = minY;
		}
		mCurY = y;
		// if (onScrollListener != null) {
		// onScrollListener.onScroll(y, maxY);
		// }
		super.scrollTo(x, y);
	}

	private void initOrResetVelocityTracker() {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		} else {
			mVelocityTracker.clear();
		}
	}

	private void initVelocityTrackerIfNotExists() {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
	}

	// private void recycleVelocityTracker() {
	// if (mVelocityTracker != null) {
	// mVelocityTracker.recycle();
	// mVelocityTracker = null;
	// }
	// }

	private void checkIsClickHead(int downY, int headHeight, int scrollY) {
		isClickHead = downY + scrollY <= headHeight;
	}

	private void checkIsClickHeadExpand(int downY, int headHeight, int scrollY) {
		if (mExpandHeight <= 0) {
			isClickHeadExpand = false;
		}
		isClickHeadExpand = downY + scrollY <= headHeight + mExpandHeight;
	}

	/**
	 * 扩大头部点击滑动范围
	 *
	 * @param expandHeight
	 */
	public void setClickHeadExpand(int expandHeight) {
		mExpandHeight = expandHeight;
	}

	private int calcDuration(int duration, int timepass) {
		return duration - timepass;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mHeadView = getChildAt(0);
		measureChildWithMargins(mHeadView, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);
		maxY = mHeadView.getMeasuredHeight() - stickyOffset;
		mHeadHeight = mHeadView.getMeasuredHeight() - stickyOffset;
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + maxY, MeasureSpec.EXACTLY));
	}

	public int getMaxY() {
		return maxY;
	}

	@Override
	protected void onFinishInflate() {
		if (mHeadView != null && !mHeadView.isClickable()) {
			mHeadView.setClickable(true);
		}
		int childCount = getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				View childAt = getChildAt(i);
				if (childAt != null) {
					if (childAt instanceof ViewGroup) {
						mViewGroup = (ViewGroup) childAt;
					}
				}
			}
		}
		super.onFinishInflate();
	}

	public boolean isClickHeadExpand() {
		return isClickHeadExpand;
	}

	public void setClickHeadExpand(boolean isClickHeadExpand) {
		this.isClickHeadExpand = isClickHeadExpand;
	}

	public boolean isSmoothTop() {
		return isSmoothTop;
	}

	public ViewGroup getViewGroup() {
		return mViewGroup;
	}

	public interface ScrollLayoutListener {
		void onScrollLayout(ScrollableLayout view, boolean isSmoothTop, int scrollDistence);
	}

	private ScrollLayoutListener mScrollLayoutListener;

	public void setScrollLayoutListener(ScrollLayoutListener mScrollLayoutListener) {
		this.mScrollLayoutListener = mScrollLayoutListener;
	}

	public void setStickyOffset(int stickyOffset) {
		this.stickyOffset = stickyOffset;
		maxY -= stickyOffset;
		mHeadHeight -= stickyOffset;
	}

}
