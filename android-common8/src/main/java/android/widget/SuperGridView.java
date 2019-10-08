package android.widget;

import android.application.CrashHandler;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.utils.ShowLog;
import android.view.View;

public class SuperGridView extends GridView {

	private boolean isSetGridViewLine;
	private int colorResID;

	public SuperGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SuperGridView(Context context) {
		super(context);
	}

	public SuperGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (isSetGridViewLine) {
			try {
				View localView1 = getChildAt(0);
				int column = getWidth() / localView1.getWidth();
				int childCount = getChildCount();
				Paint localPaint;
				localPaint = new Paint();
				localPaint.setStrokeWidth(2);
				localPaint.setStyle(Paint.Style.STROKE);
				localPaint.setColor(colorResID);
				for (int i = 0; i < childCount; i++) {
					View cellView = getChildAt(i);
					if ((i + 1) % column == 0) {
						canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
					} else if ((i + 1) > (childCount - (childCount % column))) {
						canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
					} else {
						canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
						canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
					}
				}
				if (childCount % column != 0) {
					for (int j = 0; j < (column - childCount % column); j++) {
						View lastView = getChildAt(childCount - 1);
						canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth() * j, lastView.getBottom(), localPaint);
					}
				}
			} catch (Exception e) {
				ShowLog.i(SuperGridView.class.getSimpleName(), CrashHandler.crashToString(e));
			}
		}
	}

	/**
	 * 设置GridView网格线
	 * @author :Atar
	 * @createTime:2016-11-21下午5:17:49
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param colorResID
	 * @description:
	 */
	public void setLineColorResID(int colorResID) {
		isSetGridViewLine = true;
		this.colorResID = colorResID;
	}
}
