package android.widget;

import android.content.Context;
import android.util.AttributeSet;

public class SuperListView extends ListView {

	public SuperListView(Context context) {
		super(context);
	}

	public SuperListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SuperListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
