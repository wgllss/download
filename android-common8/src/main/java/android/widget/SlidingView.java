package android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class SlidingView extends ViewGroup {

	private FrameLayout mContainer;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private static final int SNAP_VELOCITY = 1000;
	private View leftView;
	private View rightView;
	private boolean mIsBeingDragged;
	private boolean tCanSlideLeft = true;
	private boolean tCanSlideRight = false;
	private boolean hasClickLeft = false;
	private boolean hasClickRight = false;
	private boolean canSlideLeft = true;
	private boolean canSlideRight = false;
	@SuppressWarnings("unused")
	private boolean mIsAlreadySetViewState = true;

	public SlidingView(Context context) {
		super(context);
		init();
	}

	public SlidingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlidingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer.measure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		mContainer.layout(0, 0, width, height);
	}

	private void init() {
		mContainer = new FrameLayout(getContext());
		mContainer.setBackgroundColor(0xff000000);
		mScroller = new Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		super.addView(mContainer);
	}

	public void setView(View v) {
		if (mContainer.getChildCount() > 0) {
			mContainer.removeAllViews();
		}
		mContainer.addView(v);
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				if (oldX != x || oldY != y) {
					scrollTo(x, y);
				}
				// Keep on drawing until the animation has finished.
				invalidate();
			} else {
				clearChildrenCache();
			}
		} else {
			clearChildrenCache();
		}
	}

	public void setCanSliding(boolean left, boolean right) {
		canSlideLeft = left;
		canSlideRight = right;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mIsAlreadySetViewState = false;
			mLastMotionX = x;
			mLastMotionY = y;
			mIsBeingDragged = false;
			if (canSlideLeft) {
				leftView.setVisibility(View.VISIBLE);
				rightView.setVisibility(View.INVISIBLE);
			}
			if (canSlideRight) {
				leftView.setVisibility(View.INVISIBLE);
				rightView.setVisibility(View.VISIBLE);
			}
			break;

		case MotionEvent.ACTION_MOVE:
			final float dx = x - mLastMotionX;
			final float xDiff = Math.abs(dx);
			final float yDiff = Math.abs(y - mLastMotionY);
			if (xDiff > mTouchSlop && xDiff > yDiff) {
				if (canSlideLeft) {
					float oldScrollX = getScrollX();
					if (oldScrollX < 0) {
						mIsBeingDragged = true;
						mLastMotionX = x;
					} else {
						if (dx > 0) {
							mIsBeingDragged = true;
							mLastMotionX = x;
						}
					}
				} else if (canSlideRight) {
					float oldScrollX = getScrollX();
					if (oldScrollX > 0) {
						mIsBeingDragged = true;
						mLastMotionX = x;
					} else {
						if (dx < 0) {
							mIsBeingDragged = true;
							mLastMotionX = x;
						}
					}
				}
			}
			break;
		}
		return mIsBeingDragged;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			if (getScrollX() == -getLeftViewWidth() && mLastMotionX < getLeftViewWidth()) {
				return false;
			}

			if (getScrollX() == getRightViewWidth() && mLastMotionX > getLeftViewWidth()) {
				return false;
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (mIsBeingDragged) {
				enableChildrenCache();
				final float deltaX = mLastMotionX - x;
				mLastMotionX = x;
				float oldScrollX = getScrollX();
				float scrollX = oldScrollX + deltaX;
				if (canSlideLeft) {
					if (scrollX > 0)
						scrollX = 0;
				}
				if (canSlideRight) {
					if (scrollX < 0)
						scrollX = 0;
				}
				if (deltaX < 0 && oldScrollX < 0) { // left view
					final float leftBound = 0;
					final float rightBound = -getLeftViewWidth();
					if (scrollX > leftBound) {
						scrollX = leftBound;
					} else if (scrollX < rightBound) {
						scrollX = rightBound;
					}
					// mDetailView.setVisibility(View.INVISIBLE);
					// mMenuView.setVisibility(View.VISIBLE);
				} else if (deltaX > 0 && oldScrollX > 0) { // right view
					final float rightBound = getRightViewWidth();
					final float leftBound = 0;
					if (scrollX < leftBound) {
						scrollX = leftBound;
					} else if (scrollX > rightBound) {
						scrollX = rightBound;
					}
					// mDetailView.setVisibility(View.VISIBLE);
					// mMenuView.setVisibility(View.INVISIBLE);
				}
				if ((int) scrollX != 0) {
					scrollTo((int) scrollX, getScrollY());
				}

				if (scrollX > 0) {
					if (leftView != null) {
						leftView.setVisibility(View.GONE);
						leftView.clearFocus();
					}
					if (rightView != null) {
						rightView.setVisibility(View.VISIBLE);
						rightView.requestFocus();
					}

				} else {
					if (leftView != null) {
						leftView.setVisibility(View.VISIBLE);
						leftView.requestFocus();
					}
					if (rightView != null) {
						rightView.setVisibility(View.GONE);
						rightView.clearFocus();
					}
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (mIsBeingDragged) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityX = (int) velocityTracker.getXVelocity();
				velocityX = 0;
				int oldScrollX = getScrollX();
				int dx = 0;
				if (oldScrollX < 0 && canSlideLeft) {
					// 左边
					if (oldScrollX < -getLeftViewWidth() / 2 || velocityX > SNAP_VELOCITY) {
						// 左侧页面划出
						dx = -getLeftViewWidth() - oldScrollX;
						if (mOnScrollOpenListener != null) {
							mOnScrollOpenListener.onScrollOpen(this);
						}

					} else if (oldScrollX >= -getLeftViewWidth() / 2 || velocityX < -SNAP_VELOCITY) {
						// 左侧页面关闭
						dx = -oldScrollX;
						if (hasClickLeft) {
							hasClickLeft = false;
							setCanSliding(tCanSlideLeft, tCanSlideRight);
						}
						if (mOnScrollCloseListener != null) {
							mOnScrollCloseListener.onScrollClose(this);
						}
					}
				}
				if (oldScrollX >= 0 && canSlideRight) {
					// 右边
					if (oldScrollX > getRightViewWidth() / 2 || velocityX < -SNAP_VELOCITY) {
						// 右侧页面划出
						dx = getRightViewWidth() - oldScrollX;

						if (mOnScrollOpenListener != null) {
							mOnScrollOpenListener.onScrollOpen(this);
						}

					} else if (oldScrollX <= getRightViewWidth() / 2 || velocityX > SNAP_VELOCITY) {
						// 右侧页面关闭
						dx = -oldScrollX;
						if (hasClickRight) {
							hasClickRight = false;
							setCanSliding(tCanSlideLeft, tCanSlideRight);
						}
						if (mOnScrollCloseListener != null) {
							mOnScrollCloseListener.onScrollClose(this);
						}
					}
				}

				smoothScrollTo(dx);
				clearChildrenCache();

			}

			break;

		}
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}

		return true;
	}

	private int getLeftViewWidth() {
		if (leftView == null) {
			return 0;
		}
		return leftView.getWidth();
	}

	private int getRightViewWidth() {
		if (rightView == null) {
			return 0;
		}
		return rightView.getWidth();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public View getRightView() {
		return rightView;
	}

	public void setRightView(View mDetailView) {
		this.rightView = mDetailView;
	}

	public View getLeftView() {
		return leftView;
	}

	public void setLeftView(View mMenuView) {
		this.leftView = mMenuView;
	}

	void toggle() {
		int menuWidth = leftView.getWidth();
		int oldScrollX = getScrollX();
		if (oldScrollX == 0) {
			smoothScrollTo(-menuWidth);
		} else if (oldScrollX == -menuWidth) {
			smoothScrollTo(menuWidth);
		}
	}

	public void showLeftView() {
		if (leftView != null) {
			leftView.setVisibility(View.VISIBLE);
		}
		if (rightView != null) {
			rightView.setVisibility(View.GONE);
		}

		int leftWidth = getLeftViewWidth();
		int oldScrollX = getScrollX();
		if (oldScrollX == 0) {
			smoothScrollTo(-leftWidth);
			tCanSlideLeft = canSlideLeft;
			tCanSlideRight = canSlideRight;
			hasClickLeft = true;
			setCanSliding(true, false);
		} else if (oldScrollX == -leftWidth) {
			smoothScrollTo(leftWidth);
			if (hasClickLeft) {
				hasClickLeft = false;
				setCanSliding(tCanSlideLeft, tCanSlideRight);
			}
		}
	}

	public void showRightView() {
		if (leftView != null) {
			leftView.setVisibility(View.GONE);
			leftView.clearFocus();
		}
		if (rightView != null) {
			rightView.setVisibility(View.VISIBLE);
			rightView.requestFocus();
		}

		int rightWidth = getRightViewWidth();
		int oldScrollX = getScrollX();
		if (oldScrollX == 0) {
			smoothScrollTo(rightWidth);
			tCanSlideLeft = canSlideLeft;
			tCanSlideRight = canSlideRight;
			hasClickRight = true;
			setCanSliding(false, true);
		} else if (oldScrollX == rightWidth) {
			smoothScrollTo(-rightWidth);
			if (hasClickRight) {
				hasClickRight = false;
				setCanSliding(tCanSlideLeft, tCanSlideRight);
			}
		}
	}

	public void showCenterView() {
		int rightWidth = getRightViewWidth();
		int oldScrollXRight = getScrollX();
		if (oldScrollXRight == rightWidth) {
			showRightView();
		}
		int leftWidth = getLeftViewWidth();
		int oldScrollXLeft = getScrollX();
		if (oldScrollXLeft == -leftWidth) {
			showLeftView();
		}

		if (mOnScrollCloseListener != null) {
			mOnScrollCloseListener.onScrollClose(this);
		}
	}

	void smoothScrollTo(int dx) {
		int duration = 500;
		int oldScrollX = getScrollX();
		mScroller.startScroll(oldScrollX, getScrollY(), dx, getScrollY(), duration);
		invalidate();
	}

	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(true);
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}

	private OnScrollOpenListener mOnScrollOpenListener = null;
	private OnScrollCloseListener mOnScrollCloseListener = null;

	public void setOnScrollOpenListener(OnScrollOpenListener mOnScrollOpenListener) {
		this.mOnScrollOpenListener = mOnScrollOpenListener;
	}

	public void setOnScrollCloseListener(OnScrollCloseListener mOnScrollCloseListener) {
		this.mOnScrollCloseListener = mOnScrollCloseListener;
	}

	public interface OnScrollOpenListener {
		public void onScrollOpen(SlidingView slidingView);
	}

	public interface OnScrollCloseListener {
		public void onScrollClose(SlidingView slidingView);
	}

}
