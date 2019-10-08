package android.tween;

import android.view.View;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class CommonTraslate {
	// public static Animation inFromRightAnimation() {
	// Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
	// inFromRight.setDuration(500);
	// inFromRight.setInterpolator(new AccelerateInterpolator());
	// return inFromRight;
	// }
	//
	// public static Animation outToLeftAnimation() {
	// Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
	// outtoLeft.setDuration(500);
	// outtoLeft.setInterpolator(new AccelerateInterpolator());
	// return outtoLeft;
	// }
	//
	// public static Animation inFromLeftAnimation() {
	// Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
	// inFromLeft.setDuration(500);
	// inFromLeft.setInterpolator(new AccelerateInterpolator());
	// return inFromLeft;
	// }
	//
	// public static Animation outToRightAnimation() {
	// Animation outtoRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
	// outtoRight.setDuration(500);
	// outtoRight.setInterpolator(new AccelerateInterpolator());
	// return outtoRight;
	// }
	//
	// public static Animation inFromDownAnimation() {
	// Animation intFromDown = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
	// intFromDown.setDuration(500);
	// intFromDown.setInterpolator(new AccelerateInterpolator());
	// return intFromDown;
	// }
	//
	// public static Animation outToDownAnimation() {
	// Animation outtoDown = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
	// outtoDown.setDuration(500);
	// outtoDown.setInterpolator(new AccelerateInterpolator());
	// return outtoDown;
	// }

	public static void TranslateAnimation(View v, int startX, int toX, int startY, int toY, int time, AnimationListener mAnimationListener) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(time);
		anim.setFillAfter(true);
		if (mAnimationListener != null) {
			anim.setAnimationListener(mAnimationListener);
		}
		v.startAnimation(anim);
	}
}
