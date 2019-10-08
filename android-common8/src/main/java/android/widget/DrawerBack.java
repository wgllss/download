package android.widget;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.interfaces.OnOpenDrawerCompleteListener;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

/**
 * 
 *****************************************************************************************************************************************************************************
 * 拦截手势从左向右滑动代替返回
 * @author :Atar
 * @createTime:2014-12-12上午10:05:50
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
@SuppressLint("ViewConstructor")
public class DrawerBack extends FrameLayout {
	public static final int SLIDE_TARGET_CONTENT = 0;
	public static final int SLIDE_TARGET_WINDOW = 1;
	private static final int SCROLL_DURATION = 400;
	private static final int TOUCH_TARGET_WIDTH_DIP = 30;
	private static final float TOUCH_HANDLE_WIDTH_DIP = 1.33f;// 屏幕左侧4分之3
	private boolean mAdded = false;
	private boolean mDrawerEnabled = true;
	private boolean mDrawerOpened = false;
	private boolean mDrawerMoving = false;
	private boolean mGestureStarted = false;
	private int mDecorOffsetX = 0;
	private int mGestureStartX;
	private int mGestureCurrentX;
	private int mGestureStartY;
	private int mGestureCurrentY;
	private int mSlideTarget;
	private int mTouchTargetWidth;
	private Handler mScrollerHandler;
	private Scroller mScroller;
	private ViewGroup mDecorView;
	private ViewGroup mContentTarget;
	private ViewGroup mContentTargetParent;
	private ViewGroup mWindowTarget;
	private ViewGroup mWindowTargetParent;
	private ViewGroup mDecorContent;
	private ViewGroup mDecorContentParent;
	private ViewGroup mDrawerContent;
	private VelocityTracker mVelocityTracker;
	private OnOpenDrawerCompleteListener mOnOpenDrawerCompleteListener;

	private float mLastMotionX;
	private float mLastMotionY;

	@SuppressWarnings("unused")
	private float qx, qy;

	public static class SmoothInterpolator implements Interpolator {
		@Override
		public float getInterpolation(float v) {
			return (float) (Math.pow((double) v - 1.0, 5.0) + 1.0f);
		}
	}

	@SuppressWarnings("deprecation")
	public void reconfigureViewHierarchy() {
		if (mDecorView == null) {
			return;
		}
		if (mDrawerContent != null) {
			removeView(mDrawerContent);
		}
		if (mDecorContent != null) {
			removeView(mDecorContent);
			mDecorContentParent.addView(mDecorContent);
			mDecorContent.setOnClickListener(null);
			mDecorContent.setBackgroundColor(Color.TRANSPARENT);
		}
		if (mAdded) {
			mDecorContentParent.removeView(this);
		}
		if (mSlideTarget == SLIDE_TARGET_CONTENT) {
			mDecorContent = mContentTarget;
			mDecorContentParent = mContentTargetParent;
		} else if (mSlideTarget == SLIDE_TARGET_WINDOW) {
			mDecorContent = mWindowTarget;
			mDecorContentParent = mWindowTargetParent;
		} else {
			throw new IllegalArgumentException("Slide target must be one of SLIDE_TARGET_CONTENT or SLIDE_TARGET_WINDOW.");
		}
		((ViewGroup) mDecorContent.getParent()).removeView(mDecorContent);
		addView(mDrawerContent);
		addView(mDecorContent, new LayoutParams(FILL_PARENT, FILL_PARENT));
		mDecorContentParent.addView(this);
		mAdded = true;
		mDecorContent.setBackgroundColor(Color.TRANSPARENT);
		mDecorContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
	}

	@SuppressWarnings("deprecation")
	public DrawerBack(Activity activity) {
		super(activity);
		final DisplayMetrics dm = activity.getResources().getDisplayMetrics();
		mTouchTargetWidth = dm.widthPixels / TOUCH_TARGET_WIDTH_DIP;
		mScrollerHandler = new Handler();
		mScroller = new Scroller(activity, new SmoothInterpolator());
		mSlideTarget = SLIDE_TARGET_WINDOW;
		mDecorView = (ViewGroup) activity.getWindow().getDecorView();
		mWindowTarget = (ViewGroup) mDecorView.getChildAt(0);
		mWindowTargetParent = (ViewGroup) mWindowTarget.getParent();
		mContentTarget = (ViewGroup) mDecorView.findViewById(android.R.id.content);
		mContentTargetParent = (ViewGroup) mContentTarget.getParent();
		mDrawerContent = new LinearLayout(getContext());
		mDrawerContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		mDrawerContent.setBackgroundColor(Color.parseColor("#00000000"));
		reconfigureViewHierarchy();
		mDrawerContent.setPadding(0, 0, mTouchTargetWidth, 0);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		Rect windowRect = new Rect();
		mDecorView.getWindowVisibleDisplayFrame(windowRect);

		if (mSlideTarget == SLIDE_TARGET_WINDOW) {
			mDrawerContent.layout(left, top + windowRect.top, right, bottom);
		} else {
			mDrawerContent.layout(left, mDecorContent.getTop(), right, bottom);
		}
		mDecorContent.layout(mDecorContent.getLeft(), mDecorContent.getTop(), mDecorContent.getLeft() + right, bottom);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final ViewConfiguration vc = ViewConfiguration.get(getContext());
		final int widthPixels = getResources().getDisplayMetrics().widthPixels;
		final float touchThreshold = widthPixels / TOUCH_HANDLE_WIDTH_DIP;
		final double hypo;
		final boolean overcameSlop;
		if (!mDrawerEnabled) {
			return false;
		}
		final float x = ev.getX();
		final float y = ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mGestureStartX = mGestureCurrentX = (int) (ev.getX() + 0.5f);
			mGestureStartY = mGestureCurrentY = (int) (ev.getY() + 0.5f);
			if (mGestureStartX < touchThreshold && !mDrawerOpened) {
				mGestureStarted = true;
			}

			if (mGestureStartX > widthPixels - mTouchTargetWidth && mDrawerOpened) {
				mGestureStarted = true;
			}
			return false;
		case MotionEvent.ACTION_MOVE:
			final float dx = x - mLastMotionX;
			final float dy = y - mLastMotionY;
			final float xDiff = Math.abs(dx);
			final float yDiff = Math.abs(y - mLastMotionY);
			if (xDiff > vc.getScaledTouchSlop() && Math.abs(xDiff) > Math.abs(yDiff) && Math.abs(dy) < Math.abs(vc.getScaledTouchSlop() / 1)) {
			} else {
				return false;
			}
			if (!mGestureStarted) {
				return false;
			}
			if (!mDrawerOpened && (ev.getX() < mGestureCurrentX || ev.getX() < mGestureStartX)) {
				return (mGestureStarted = false);
			}
			mGestureCurrentX = (int) (ev.getX() + 0.5f);
			mGestureCurrentY = (int) (ev.getY() + 0.5f);
			hypo = Math.hypot(mGestureCurrentX - mGestureStartX, mGestureCurrentY - mGestureStartY);
			overcameSlop = hypo > vc.getScaledTouchSlop();
			return overcameSlop;
		case MotionEvent.ACTION_UP:
			if (mGestureStartX > widthPixels - mTouchTargetWidth && mDrawerOpened) {
				closeDrawer();
			}
			mGestureStarted = false;
			mGestureStartX = mGestureCurrentX = -1;
			mGestureStartY = mGestureCurrentY = -1;
			return false;
		}

		return false;
	}

	@SuppressLint("Recycle")
	@SuppressWarnings("unused")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final ViewConfiguration vc = ViewConfiguration.get(getContext());
		final int widthPixels = getResources().getDisplayMetrics().widthPixels;
		final int deltaX = (int) (event.getX() + 0.5f) - mGestureCurrentX;
		final int deltaY = (int) (event.getY() + 0.5f) - mGestureCurrentY;
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		mGestureCurrentX = (int) (event.getX() + 0.5f);
		mGestureCurrentY = (int) (event.getY() + 0.5f);
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			mDrawerMoving = true;
			if (mDecorOffsetX + deltaX > widthPixels - mTouchTargetWidth) {
				if (mDecorOffsetX != widthPixels - mTouchTargetWidth) {
					mDrawerOpened = true;
					mDecorContent.offsetLeftAndRight(widthPixels - mTouchTargetWidth - mDecorOffsetX);
					mDecorOffsetX = widthPixels - mTouchTargetWidth;
					invalidate();
				}
			} else if (mDecorOffsetX + deltaX < 0) {
				if (mDecorOffsetX != 0) {
					mDrawerOpened = false;
					mDecorContent.offsetLeftAndRight(0 - mDecorContent.getLeft());
					mDecorOffsetX = 0;
					invalidate();
				}
			} else {
				if (mOnOpenDrawerCompleteListener != null && mOnOpenDrawerCompleteListener.onMoveRight()) {

				} else {
					mDecorContent.offsetLeftAndRight(deltaX);
					mDecorOffsetX += deltaX;
					invalidate();
					return false;
				}
			}
			return true;
		case MotionEvent.ACTION_UP:
			qx = mGestureCurrentX;
			qy = mGestureCurrentY;
			mGestureStarted = false;
			mDrawerMoving = false;
			mVelocityTracker.computeCurrentVelocity(1000);
			if (Math.abs(mVelocityTracker.getXVelocity()) > vc.getScaledMinimumFlingVelocity()) {
				if (mVelocityTracker.getXVelocity() > 0 && mDecorOffsetX > (widthPixels / 5.0)) {
					mDrawerOpened = false;
					openDrawer();
					// mDrawerContent.setBackgroundColor(Color.parseColor("#00000000"));
				} else {
					mDrawerOpened = true;
					// mDrawerContent.setBackgroundColor(Color.parseColor("#40000000"));
					closeDrawer();
				}
			} else {
				if (mDecorOffsetX > (widthPixels / 5.0)) {
					mDrawerOpened = false;
					openDrawer();
					// mDrawerContent.setBackgroundColor(Color.parseColor("#00000000"));
				} else {
					mDrawerOpened = true;
					// mDrawerContent.setBackgroundColor(Color.parseColor("#40000000"));
					closeDrawer();
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mDrawerOpened || mDrawerMoving) {
			canvas.save();
			canvas.translate(mDecorOffsetX, 0);
			canvas.restore();
		}
	}

	public void setOnDrawerBackEnabled(final boolean enabled) {
		mDrawerEnabled = enabled;
	}

	public boolean isDrawerEnabled() {
		return mDrawerEnabled;
	}

	public void toggleDrawer(final boolean animate) {
		if (!mDrawerOpened) {
			openDrawer(animate);
		} else {
			closeDrawer(animate);
		}
	}

	public void toggleDrawer() {
		toggleDrawer(true);
	}

	public void openDrawer(final boolean animate) {
		if (mDrawerOpened || mDrawerMoving) {
			return;
		}
		mDrawerMoving = true;
		final int widthPixels = getResources().getDisplayMetrics().widthPixels;
		mScroller.startScroll(mDecorOffsetX, 0, widthPixels - mDecorOffsetX, 0, animate ? SCROLL_DURATION : 0);
		mScrollerHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				final boolean scrolling = mScroller.computeScrollOffset();
				mDecorContent.offsetLeftAndRight(mScroller.getCurrX() - mDecorOffsetX);
				mDecorOffsetX = mScroller.getCurrX();
				postInvalidate();
				if (!scrolling) {
					mDrawerMoving = false;
					mDrawerOpened = true;
					mScrollerHandler.post(new Runnable() {
						@Override
						public void run() {
							enableDisableViewGroup(mDecorContent, false);
							if (mOnOpenDrawerCompleteListener != null) {
								mOnOpenDrawerCompleteListener.onOpenDrawerComplete();
							}
						}
					});
				} else {
					mScrollerHandler.postDelayed(this, 16);
				}
			}
		}, 16);
	}

	public void openDrawer() {
		openDrawer(true);
	}

	@SuppressWarnings("unused")
	public void closeDrawer(final boolean animate) {
		if (!mDrawerOpened || mDrawerMoving) {
			return;
		}
		mDrawerMoving = true;
		final int widthPixels = getResources().getDisplayMetrics().widthPixels;
		mScroller.startScroll(mDecorOffsetX, 0, -mDecorOffsetX, 0, animate ? SCROLL_DURATION : 0);
		mScrollerHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				final boolean scrolling = mScroller.computeScrollOffset();
				mDecorContent.offsetLeftAndRight(mScroller.getCurrX() - mDecorOffsetX);
				mDecorOffsetX = mScroller.getCurrX();
				postInvalidate();
				if (!scrolling) {
					mDrawerMoving = false;
					mDrawerOpened = false;
					// if (mDrawerCallbacks != null) {
					// mScrollerHandler.post(new Runnable() {
					// @Override
					// public void run() {
					// enableDisableViewGroup(mDecorContent, true);
					// mDrawerCallbacks.onDrawerClosed();
					// }
					// });
					// }
				} else {
					mScrollerHandler.postDelayed(this, 16);
				}
			}
		}, 16);
	}

	public void closeDrawer() {
		closeDrawer(true);
	}

	public boolean isDrawerOpened() {
		return mDrawerOpened;
	}

	public boolean isDrawerMoving() {
		return mDrawerMoving;
	}

	public void setOnOpenDrawerCompleteListener(final OnOpenDrawerCompleteListener mOnOpenDrawerCompleteListener) {
		this.mOnOpenDrawerCompleteListener = mOnOpenDrawerCompleteListener;
	}

	public int getSlideTarget() {
		return mSlideTarget;
	}

	public void setSlideTarget(final int slideTarget) {
		if (mSlideTarget != slideTarget) {
			mSlideTarget = slideTarget;
			reconfigureViewHierarchy();
		}
	}

	public void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
		int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = viewGroup.getChildAt(i);
			if (view.isFocusable()) {
				view.setEnabled(enabled);
			}
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, enabled);
			} else if (view instanceof ListView) {
				if (view.isFocusable()) {
					view.setEnabled(enabled);
				}
				ListView listView = (ListView) view;
				int listChildCount = listView.getChildCount();
				for (int j = 0; j < listChildCount; j++) {
					if (view.isFocusable()) {
						listView.getChildAt(j).setEnabled(false);
					}
				}
			}
		}
	}
}