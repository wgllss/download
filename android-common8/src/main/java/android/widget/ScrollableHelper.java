package android.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.webkit.WebView;

@TargetApi(Build.VERSION_CODES.DONUT)
public class ScrollableHelper {

	private ScrollableContainer mCurrentScrollableCainer;
	private int sysVersion = Build.VERSION.SDK_INT;

	public interface ScrollableContainer {
		View getScrollableView();
	}

	public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer) {
		this.mCurrentScrollableCainer = scrollableContainer;
	}

	public View getScrollableView() {
		if (mCurrentScrollableCainer == null) {
			return null;
		}
		return mCurrentScrollableCainer.getScrollableView();
	}

	@SuppressWarnings("rawtypes")
	public boolean isTop() {
		View scrollableView = getScrollableView();
		if (scrollableView == null) {
			throw new NullPointerException("You should call ScrollableHelper.setCurrentScrollableContainer() to set ScrollableContainer.");
		}

		if (scrollableView instanceof AdapterView) {
			return isAdapterViewTop((AdapterView) scrollableView);
		} else if (scrollableView instanceof ScrollView) {
			return isScrollViewTop((ScrollView) scrollableView);
			// } else if (scrollableView instanceof RecyclerView) {
			// return isRecyclerViewTop((RecyclerView) scrollableView);
		} else if (scrollableView instanceof TextView || scrollableView instanceof ImageView) {
			return true;
		} else if (scrollableView instanceof WebView) {
			return ((WebView) scrollableView).getScrollY() <= 0;
		}
		throw new IllegalStateException("scrollableView must be a instance of AdapterView|ScrollView|RecyclerView");
	}

	// public static boolean isRecyclerViewTop(RecyclerView recyclerView) {
	// if (recyclerView != null) {
	// RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
	// if (layoutManager instanceof LinearLayoutManager) {
	// int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
	// View childAt = recyclerView.getChildAt(0);
	// if (childAt == null || (firstVisibleItemPosition == 0 && childAt != null && childAt.getTop() == 0)) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	public static boolean isAdapterViewTop(AdapterView<?> adapterView) {
		final Adapter adapter = adapterView.getAdapter();
		if (null == adapter || adapter.isEmpty()) {
			return true;
		} else {
			if (adapterView.getFirstVisiblePosition() <= 1) {
				final View firstVisibleChild = adapterView.getChildAt(0);
				if (firstVisibleChild != null) {
					return firstVisibleChild.getTop() >= adapterView.getTop();
				}
			}
		}
		return false;
	}

	public static boolean isScrollViewTop(ScrollView scrollView) {
		if (scrollView != null) {
			int scrollViewY = scrollView.getScrollY();
			return scrollViewY <= 0;
		}
		return false;
	}

	public boolean isButtom() {
		View scrollableView = getScrollableView();
		if (scrollableView == null) {
			throw new NullPointerException("You should call ScrollableHelper.setCurrentScrollableContainer() to set ScrollableContainer.");
		}
		if (scrollableView instanceof AdapterView) {
			AdapterView<?> mAdapterView = (AdapterView<?>) scrollableView;
			if (mAdapterView != null) {
				final Adapter adapter = mAdapterView.getAdapter();
				if (null == adapter || adapter.isEmpty()) {
					return true;
				} else {
					final int lastItemPosition = mAdapterView.getCount() - 1;
					final int lastVisiblePosition = mAdapterView.getLastVisiblePosition();
					if (lastVisiblePosition >= lastItemPosition - 1) {
						final int childIndex = lastVisiblePosition - mAdapterView.getFirstVisiblePosition();
						final View lastVisibleChild = mAdapterView.getChildAt(childIndex);
						if (lastVisibleChild != null) {
							return lastVisibleChild.getBottom() <= mAdapterView.getBottom();
						}
					}
				}
				return false;
			}
		}
		return false;
	}

	@SuppressLint("NewApi")
	public void smoothScrollBy(int velocityY, int distance, int duration) {
		View scrollableView = getScrollableView();
		if (scrollableView instanceof AbsListView) {
			AbsListView absListView = (AbsListView) scrollableView;
			if (sysVersion >= 21) {
				absListView.fling(velocityY);
			} else {
				absListView.smoothScrollBy(distance, duration);
			}
		} else if (scrollableView instanceof ScrollView) {
			((ScrollView) scrollableView).fling(velocityY);
			// } else if (scrollableView instanceof RecyclerView) {
			// ((RecyclerView) scrollableView).fling(0, velocityY);
		} else if (scrollableView instanceof WebView) {
			((WebView) scrollableView).flingScroll(0, velocityY);
		}
	}
}
