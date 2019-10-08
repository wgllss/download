package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

/**
 * 
 *****************************************************************************************************************************************************************************
 * 线性布局水平listview 实现
 * @author :Atar
 * @createTime:2015-5-25下午1:34:58
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class HorizontalLayout extends LinearLayout {

	public HorizontalLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * LinearLayout 加入适配器
	 * @author :Atar
	 * @createTime:2017-5-23下午1:42:02
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param adapter
	 * @param isRemoveAllViews 加入前是否清除LinearLayout内所有布局
	 * @description:
	 */
	public void setAdapter(final BaseAdapter adapter, boolean isRemoveAllViews) {
		setAdapter(adapter, isRemoveAllViews, 0);
	}

	/**
	 * 
	 * @author :Atar
	 * @createTime:2017-5-23下午1:43:06
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param adapter
	 * @param isRemoveAllViews 加入前是否清除LinearLayout内所有布局
	 * @param grivaty LinearLayout 内部是否等分 or 自定义情况
	 * @description:
	 */
	public void setAdapter(final BaseAdapter adapter, boolean isRemoveAllViews, int grivaty) {
		if (isRemoveAllViews) {
			removeAllViews();
		}
		for (int i = 0; i < adapter.getCount(); i++) {
			View view = adapter.getView(i, null, null);
			this.setOrientation(HORIZONTAL);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if (grivaty > 0) {
				lp.weight = grivaty;
				lp.width = LayoutParams.MATCH_PARENT;
				lp.gravity = Gravity.CENTER;
			}
			this.addView(view, lp);
		}
	}
}
