/**
 * 
 */
package android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *****************************************************************************************************************************************************************************
 * Viewpager禁用/开启滑动切换功能
 * @author :Atar
 * @createTime:2016-4-1上午10:34:31
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
@SuppressLint("ClickableViewAccessibility")
public class ScrollbleViewPager extends ViewPager {
	private boolean scrollble = true;
	private float mLastMotionX;
	private float mLastMotionY;

	public ScrollbleViewPager(Context context) {
		super(context);
	}

	public ScrollbleViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!scrollble) {
			final float x = ev.getX();
			final float y = ev.getY();
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionX = x;
				mLastMotionY = y;
				return super.onInterceptTouchEvent(ev);
			case MotionEvent.ACTION_MOVE:
				final float dx = x - mLastMotionX;
				final float dy = y - mLastMotionY;
				final float xDiff = Math.abs(dx);
				final float yDiff = Math.abs(dy);
				if (xDiff > yDiff) {
					return false;
				}
			case MotionEvent.ACTION_UP:
				return super.onInterceptTouchEvent(ev);
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	public boolean isScrollble() {
		return scrollble;
	}

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}
}
