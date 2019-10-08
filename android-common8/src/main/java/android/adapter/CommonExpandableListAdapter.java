/**
 * 
 */
package android.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 *****************************************************************************************************************************************************************************
 * 自定义抽象 BaseExpandableListAdapter 带入泛型
 * @author :Atar
 * @createTime:2015-5-4下午6:02:16
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:只需要在需要的地方继承 CommonExpandableListAdapter 可一次性加载，可单独加载子项 泛型 G:组对象 C:子对象
 *****************************************************************************************************************************************************************************
 */
public abstract class CommonExpandableListAdapter<G, C> extends BaseExpandableListAdapter {
	private List<G> listGroup;
	private List<List<C>> listChild = new ArrayList<List<C>>();
	private Handler handler;
	private Context context;
	private int condition = -1;
	private int skinType;

	/**
	 * 构造方法只带组List
	 * @param listGroup
	 */
	public CommonExpandableListAdapter(List<G> listGroup) {
		this.listGroup = listGroup;
	}

	/**
	 * 构造方法带组List 和context
	 * @param listGroup
	 * @param context
	 */
	public CommonExpandableListAdapter(List<G> listGroup, Context context) {
		this.listGroup = listGroup;
		this.context = context;
	}

	/**
	 * 构造方法带 组List 和handler 和 context
	 * @param listGroup
	 * @param context 
	 * @param handler
	 */
	public CommonExpandableListAdapter(List<G> listGroup, Context context, Handler handler) {
		this.listGroup = listGroup;
		this.handler = handler;
	}

	/**
	 * 构造方法带 组List 和handler
	 * @param listGroup
	 * @param handler
	 */
	public CommonExpandableListAdapter(List<G> listGroup, Handler handler) {
		this.listGroup = listGroup;
		this.handler = handler;
	}

	@Override
	public void notifyDataSetChanged() {
		if (listChild != null && listGroup != null && listChild.size() < listGroup.size()) {
			for (int i = listChild.size() - 1; i < listGroup.size(); i++) {
				if (i >= 0) {
					List<C> childTemp = new ArrayList<C>();
					listChild.add(childTemp);
				}
			}
		}
		super.notifyDataSetChanged();
	}

	/**
	 * 设置指定位置下子项数据 
	 * @author :Atar
	 * @createTime:2015-5-5上午9:19:36
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param groupPosition
	 * @param childList
	 * @description:
	 */
	public void setChildList(int groupPosition, List<C> childList) {
		if (listChild.size() == 0) {
			for (int i = 0; i < getGroupCount(); i++) {
				List<C> childTemp = new ArrayList<C>();
				listChild.add(childTemp);
			}
		}
		listChild.set(groupPosition, childList);
		notifyDataSetChanged();
	}

	/**
	 * 添加到指定位置下子项数据 
	 * @author :Atar
	 * @createTime:2015-5-5上午9:19:36
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param groupPosition
	 * @param childList
	 * @description:
	 */
	public void addToChildList(int groupPosition, List<C> childList) {
		if (listChild.size() == 0) {
			for (int i = 0; i < getGroupCount(); i++) {
				List<C> childTemp = new ArrayList<C>();
				listChild.add(childTemp);
			}
		}
		listChild.get(groupPosition).addAll(childList);
		notifyDataSetChanged();
	}

	public CommonExpandableListAdapter(List<G> listGroup, List<List<C>> listChild) {
		this.listGroup = listGroup;
		this.listChild.addAll(listChild);
	}

	public CommonExpandableListAdapter(List<G> listGroup, List<List<C>> listChild, Context context) {
		this.listGroup = listGroup;
		this.listChild.addAll(listChild);
		this.context = context;
	}

	public CommonExpandableListAdapter(List<G> listGroup, List<List<C>> listChild, Handler handler) {
		this.listGroup = listGroup;
		this.listChild.addAll(listChild);
		this.handler = handler;
	}

	public CommonExpandableListAdapter(List<G> listGroup, List<List<C>> listChild, Handler handler, Context context) {
		this.listGroup = listGroup;
		this.listChild.addAll(listChild);
		this.handler = handler;
	}

	public List<G> getListGroup() {
		return listGroup;
	}

	public List<List<C>> getListChild() {
		return listChild;
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

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}

	@Override
	public C getChild(int groupPosition, int childPosition) {
		return (listChild != null && listChild.size() > 0 && groupPosition < listChild.size() && listChild.get(groupPosition) != null && listChild.get(groupPosition).size() > 0 && childPosition < listChild
				.get(groupPosition).size()) ? listChild.get(groupPosition).get(childPosition) : null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return (listChild != null && listChild.size() > 0 && groupPosition < listChild.size() && listChild.get(groupPosition) != null) ? listChild.get(groupPosition).size() : 0;
	}

	@Override
	public G getGroup(int groupPosition) {
		return (listGroup != null && listGroup.size() > 0 && groupPosition < listGroup.size()) ? listGroup.get(groupPosition) : null;
	}

	@Override
	public int getGroupCount() {
		return listGroup != null ? listGroup.size() : 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void setSkinType(int skinType) {
		this.skinType = skinType;
		notifyDataSetChanged();
	}

	public int getSkinType() {
		return skinType;
	}

	public ImageLoadingListener getAnimateFirstListener() {
		return animateFirstListener;
	}

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
