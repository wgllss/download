/**
 * 
 */
package android.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :Atar
 * @createTime:2016-8-5下午3:20:55
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
@SuppressLint({ "ClickableViewAccessibility", "HandlerLeak" })
public class HorizontalScrollPageLayout extends HorizontalScrollView {
	private int mBaseScrollX;// 滑动基线。也就是点击并滑动之前的x值，以此值计算相对滑动距离。
	private int mScreenWidth;
	private LinearLayout mContainer;
	private boolean flag;
	private boolean flagIsAdd;
	private int mPageCount;// 页面数量
	private int mScrollX = 200;// 滑动多长距离翻页
	private int currentItem;
	private static final int timerAnimation = 1;
	private long time = 6500;
	private boolean autoPlay = true;
	private OnViewChangeListener mOnViewChangeListener;

	public HorizontalScrollPageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		timer.schedule(task, time, time);
	}

	public void setOnViewChangeListener(OnViewChangeListener mOnViewChangeListener) {
		this.mOnViewChangeListener = mOnViewChangeListener;
	}

	/** 
	 * 添加一个页面到最后。 
	 * @param page 
	 */
	public void addPage(View page) {
		addPage(page, -1);
	}

	/** 
	 * 添加一个页面。 
	 * @param page 
	 */
	public void addPage(View page, int index) {
		if (!flag) {
			mContainer = (LinearLayout) getChildAt(0);
			mContainer.setOrientation(LinearLayout.HORIZONTAL);
			flag = true;
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mScreenWidth, LayoutParams.MATCH_PARENT);
		if (index == -1) {
			mContainer.addView(page, params);
		} else {
			mContainer.addView(page, index, params);
		}
		mPageCount++;
	}

	public void setAdapter(BaseAdapter adapter, boolean isRemoveAllViews) {
		if (isRemoveAllViews) {
			removeAllPages();
		}
		if (!flagIsAdd) {
			for (int i = 0; i < adapter.getCount(); i++) {
				View view = adapter.getView(i, null, null);
				addPage(view);
			}
			flagIsAdd = true;
		}
	}

	public void removePage(int index) {
		if (mPageCount < 1) {
			return;
		}
		if (index < 0 || index > mPageCount - 1) {
			return;
		}
		mContainer.removeViewAt(index);
		mPageCount--;
	}

	public void removeAllPages() {
		if (mPageCount > 0) {
			mContainer.removeAllViews();
		}
		flagIsAdd = false;
		mPageCount = 0;
	}

	public int getPageCount() {
		return mPageCount;
	}

	/** 
	 * 获取相对滑动位置。由右向左滑动，返回正值；由左向右滑动，返回负值。 
	 * @return 
	 */
	private int getBaseScrollX() {
		return getScrollX() - mBaseScrollX;
	}

	/** 
	 * 使相对于基线移动x距离。 
	 * @param x x为正值时右移；为负值时左移。 
	 */
	private void baseSmoothScrollTo(int x) {
		smoothScrollTo(x + mBaseScrollX, 0);
	}

	/**
	 * 显示第几页
	 * @author :Atar
	 * @createTime:2016-8-5下午5:16:33
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param which
	 * @description:
	 */
	public void setWhichPage(int which) {
		if (which >= 0 && which < mPageCount) {
			baseSmoothScrollTo((which - currentItem) * mScreenWidth);
			mBaseScrollX += (which - currentItem) * mScreenWidth;
			currentItem = which;
			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.onViwChange(HorizontalScrollPageLayout.this, currentItem);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			int scrollX = getBaseScrollX();
			if (scrollX > mScrollX) {// 左滑，大于一半，移到下一页
				currentItem++;
				baseSmoothScrollTo(mScreenWidth);
				mBaseScrollX += mScreenWidth;
			} else if (scrollX > 0) {// 左滑，不到一半，返回原位
				baseSmoothScrollTo(0);
			} else if (scrollX > -mScrollX) { // 右滑，不到一半，返回原位
				baseSmoothScrollTo(0);
			} else { // 右滑，大于一半，移到下一页
				currentItem--;
				baseSmoothScrollTo(-mScreenWidth);
				mBaseScrollX -= mScreenWidth;
			}
			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.onViwChange(this, currentItem);
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	private final Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case timerAnimation:
				if (currentItem + 1 == mPageCount) {
					setWhichPage(0);
				} else {
					setWhichPage(currentItem + 1);
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

	public int getCurrentItem() {
		return currentItem;
	}

	public interface OnViewChangeListener {
		void onViwChange(HorizontalScrollPageLayout view, int currentItem);
	}
}
