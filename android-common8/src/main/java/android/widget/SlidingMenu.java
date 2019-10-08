package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SlidingView.OnScrollCloseListener;
import android.widget.SlidingView.OnScrollOpenListener;

public class SlidingMenu extends RelativeLayout {

	private SlidingView centerView;
	private View leftView;
	private View rightView;
	@SuppressWarnings("unused")
	private int alignScreenWidth;
	private int alignLeftScreenWidth = 0;
	private int alignRightScreenWidth = 0;

	public SlidingMenu(Context context) {
		super(context);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void addViews(View left, View center, View right) {
		setLeftView(left);
		setRightView(right);
		setCenterView(center);
	}

	public void setAlignScreenWidth(int alignScreenWidth) {
		this.alignScreenWidth = alignScreenWidth;
	}

	public void setAlignLeftScreenWidth(int alignLeftScreenWidth) {
		this.alignLeftScreenWidth = alignLeftScreenWidth;
	}

	public void setAlignRightScreenWidth(int alignRightScreenWidth) {
		this.alignRightScreenWidth = alignRightScreenWidth;
	}

	public void setCanSliding(boolean left, boolean right) {
		centerView.setCanSliding(left, right);
	}

	@SuppressWarnings("deprecation")
	public void setLeftView(View view) {
		if (view != null) {
			LayoutParams behindParams = new LayoutParams(alignLeftScreenWidth, LayoutParams.FILL_PARENT);
			behindParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			addView(view, behindParams);
			leftView = view;
		}
	}

	@SuppressWarnings("deprecation")
	public void setRightView(View view) {
		if (view != null) {
			LayoutParams behindParams = new LayoutParams(alignRightScreenWidth, LayoutParams.FILL_PARENT);
			behindParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			addView(view, behindParams);
			rightView = view;
		}
	}

	@SuppressWarnings("deprecation")
	public void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		aboveParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		centerView = new SlidingView(getContext());
		// centerView.setBackgroundColor(Color.TRANSPARENT);
		addView(centerView, aboveParams);
		centerView.setView(view);
		centerView.invalidate();
		centerView.setLeftView(leftView);
		centerView.setRightView(rightView);
	}

	public void showLeftView() {
		centerView.showLeftView();
	}

	public void showRightView() {
		centerView.showRightView();
	}

	public void showCenterView() {
		centerView.showCenterView();
	}

	public void setOnScrollOpenListener(OnScrollOpenListener onScrollEndListener) {
		centerView.setOnScrollOpenListener(onScrollEndListener);
	}

	public void setOnScrollCloseListener(OnScrollCloseListener mOnScrollCloseListener) {
		centerView.setOnScrollCloseListener(mOnScrollCloseListener);
	}
}
