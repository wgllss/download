package android.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public abstract class CommonAdapter<T> extends BaseAdapter {

	private List<T> list;
	private Handler handler;
	private Context context;
	private int currentPostiotn = -1;
	private int condition = -1;
	private int skinType;

	@SuppressWarnings("unchecked")
	public CommonAdapter(List<?> list) {
		this.list = (List<T>) list;
	}

	// @SuppressWarnings("unchecked")
	// public CommonAdapter(List<?> list, Handler handler) {
	// this.list = (List<T>) list;
	// this.handler = handler;
	// }
	//
	@SuppressWarnings("unchecked")
	public CommonAdapter(List<?> list, Context context) {
		this.list = (List<T>) list;
		this.context = context;
	}

	public int getCount() {
		return list == null ? 0 : list.size();
	}

	public T getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void removeItem(int position) {
		list.remove(position);
		notifyDataSetChanged();
	}

	public void insertItem(T t, int position) {
		list.add(position, t);
		notifyDataSetChanged();
	}

	public void cleanList() {
		if (getCount() > 0) {
			list.clear();
			notifyDataSetChanged();
		}
	}

	// public void addSearchList(List<T> mlist) {
	// cleanList();
	// if (mlist != null && mlist.size() > 0) {
	// list.addAll(mlist);
	// notifyDataSetChanged();
	// }
	// }

	public void addRefresh(List<T> mlist) {
		cleanList();
		if (mlist != null && mlist.size() > 0) {
			list.addAll(mlist);
			notifyDataSetChanged();
		}
	}

	public void addMore(List<T> mlist) {
		if (mlist.size() > 0) {
			list.addAll(mlist);
			notifyDataSetChanged();
		}
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public int getCurrentPostiotn() {
		return currentPostiotn;
	}

	public void setCurrentPostiotn(int currentPostiotn) {
		this.currentPostiotn = currentPostiotn;
		notifyDataSetChanged();
	}

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
		notifyDataSetChanged();
	}

	public int getSkinType() {
		return skinType;
	}

	public void setSkinType(int skinType) {
		this.skinType = skinType;
		notifyDataSetChanged();
	}

	public ImageLoadingListener getAnimateFirstListener() {
		return animateFirstListener;
	}

	//
	// public void setAnimateFirstListener(ImageLoadingListener animateFirstListener) {
	// this.animateFirstListener = animateFirstListener;
	// }

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	protected ImageLoadingListener getImageLoadingListener() {
		return animateFirstListener;
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
