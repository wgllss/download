package android.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * 
 *****************************************************************************************************************************************************************************
 * 可自动播放的单一页 Gallery
 * @author :Atar
 * @createTime:2015-2-5上午11:19:03
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class SlideOnePageGallery extends Gallery {
	private float mLastMotionX;
	private float mLastMotionY;
	private static final int timerAnimation = 1;
	private long time = 3500;
	private boolean autoPlay = true;
	private final Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case timerAnimation:
				int position = getSelectedItemPosition();
				if (position >= (getCount() - 1)) {
					// setSelection(0);
					onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
				} else {
					// setSelection(position + 1);
					onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
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

	public SlideOnePageGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setStaticTransformationsEnabled(true);
		timer.schedule(task, time, time);
	}

	public SlideOnePageGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		setStaticTransformationsEnabled(true);
		timer.schedule(task, time, time);
	}

	public SlideOnePageGallery(Context context) {
		super(context);
		setStaticTransformationsEnabled(true);
		timer.schedule(task, time, time);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:// 按下事件
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			final float dx = (int) (mLastMotionX - x);
			final float dy = (int) (mLastMotionY - y);
			if (Math.abs(dx) < 6 && Math.abs(dy) < 30) {

			} else {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_MASK:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	// @Override
	// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	// if (e1 != null && e2 != null) {
	// int kEvent;
	// if (e2.getX() > e1.getX()) {
	// kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
	// } else {
	// kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
	// }
	// onKeyDown(kEvent, null);
	// }
	// return true;
	// }

	// public void onPausePlay() {
	// if (timer != null) {
	// timer.cancel();
	// }
	// }
	//
	// public void onResumePlay() {
	// if (timer != null) {
	// timer.schedule(task, 3000, 3000);
	// }
	// }

	// public boolean isAutoPlay() {
	// return autoPlay;
	// }

	// public void setAutoPlay(boolean autoPlay) {
	// this.autoPlay = autoPlay;
	// }
}
