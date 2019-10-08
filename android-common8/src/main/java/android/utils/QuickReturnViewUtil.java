/**
 * 
 */
package android.utils;

import android.annotation.SuppressLint;
import android.tween.CommonTraslate;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :Atar
 * @createTime:2017-7-17下午1:50:44
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
@SuppressLint("ClickableViewAccessibility")
public class QuickReturnViewUtil implements OnTouchListener {
	private float mDownX;
	private float mDownY;
	private float mLastY;
	private int mTouchSlop;
	private boolean flag1, flag2;

	private int returnViewHeight;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_OFFSCREEN;

	private View mQuickReturnView;

	public void setReturnView(View touchView, View returnView) {
		if (touchView == null || touchView.getContext() == null) {
			return;
		}
		this.mQuickReturnView = returnView;
		returnView.setVisibility(View.INVISIBLE);
		touchView.setOnTouchListener(this);
		ViewConfiguration configuration = ViewConfiguration.get(touchView.getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
	}

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		if (mQuickReturnView == null) {
			return false;
		}
		returnViewHeight = mQuickReturnView.getHeight();
		float currentX = ev.getX();
		float currentY = ev.getY();
		float deltaY;
		int shiftX;
		int shiftY;
		shiftX = (int) Math.abs(currentX - mDownX);
		shiftY = (int) Math.abs(currentY - mDownY);

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			flag1 = true;
			flag2 = true;
			mDownX = currentX;
			mDownY = currentY;
			mLastY = currentY;
			break;
		case MotionEvent.ACTION_MOVE:
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
			if (flag2 && shiftY > mTouchSlop && shiftY > shiftX) {
				if (deltaY <= -1) {// 下拉
					if (mState == STATE_OFFSCREEN) {
						CommonTraslate.TranslateAnimation(mQuickReturnView, 0, 0, returnViewHeight, 0, 200, new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
								mState = STATE_RETURNING;
							}

							@Override
							public void onAnimationRepeat(Animation animation) {

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								mState = STATE_ONSCREEN;
							}
						});
					}
				} else if (deltaY > 1) {// 上拉
					if (mState == STATE_ONSCREEN) {
						CommonTraslate.TranslateAnimation(mQuickReturnView, 0, 0, 0, returnViewHeight, 200, new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
								mState = STATE_RETURNING;
							}

							@Override
							public void onAnimationRepeat(Animation animation) {

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								mState = STATE_OFFSCREEN;
							}
						});
					}
				}
			}
			mLastY = currentY;
			break;
		}
		return false;
	}
}
