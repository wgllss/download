package android.tween;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation.AnimationListener;

public class CommonAlpha {
	public static void set021Alpha(View view, long l, AnimationListener mAnimationListener) {
		AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
		inAlphaAnimation.setDuration(l);
		if (mAnimationListener != null) {
			inAlphaAnimation.setAnimationListener(mAnimationListener);
		}
		view.setAnimation(inAlphaAnimation);
	}

	public static void set120Alpha(View view, long l, AnimationListener mAnimationListener) {
		AlphaAnimation inAlphaAnimation = new AlphaAnimation(1, 0);
		inAlphaAnimation.setDuration(l);
		if (mAnimationListener != null) {
			inAlphaAnimation.setAnimationListener(mAnimationListener);
		}
		view.setAnimation(inAlphaAnimation);
	}

	public static void set121Alpha(View view, long l, AnimationListener mAnimationListener) {
		AlphaAnimation inAlphaAnimation = new AlphaAnimation(1, 1);
		inAlphaAnimation.setDuration(l);
		if (mAnimationListener != null) {
			inAlphaAnimation.setAnimationListener(mAnimationListener);
		}
		view.setAnimation(inAlphaAnimation);
	}
}
