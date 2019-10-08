/**
 * 
 */
package android.tween;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :Atar
 * @createTime:2014-8-27下午4:35:36
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class CommonRotate {
	public static void Rotate(View v, float fromDegrees, float toDegrees, int time, AnimationListener listener) {
		RotateAnimation mRotateAnimation = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setDuration(time);
		if (listener != null) {
			mRotateAnimation.setAnimationListener(listener);
		}
		v.startAnimation(mRotateAnimation);
	}
}
