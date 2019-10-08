/**
 * 
 */
package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 *****************************************************************************************************************************************************************************
 * 线性布局垂直listview 实现
 * @author :Atar
 * @createTime:2015-5-25下午1:23:50
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class VerticalLayout extends LinearLayout {

	public VerticalLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setAdapter(final BaseAdapter adapter, boolean isRemoveAllViews) {
		if (isRemoveAllViews) {
			removeAllViews();
		}
		for (int i = 0; i < adapter.getCount(); i++) {
			View view = adapter.getView(i, null, null);
			this.setOrientation(VERTICAL);
			this.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}
}
