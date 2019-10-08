/**
 * 
 */
package android.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :Atar
 * @createTime:2014-6-3下午3:44:26
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class CommonPagerAdapter<T> extends PagerAdapter {
	private List<T> list;
	private Handler handler;
	private Context context;

	@SuppressWarnings("unchecked")
	public CommonPagerAdapter(List<?> list) {
		this.list = (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	public CommonPagerAdapter(List<?> list, Handler handler) {
		this.list = (List<T>) list;
		this.handler = handler;
	}

	@SuppressWarnings("unchecked")
	public CommonPagerAdapter(List<?> list, Context context) {
		this.list = (List<T>) list;
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public CommonPagerAdapter(List<?> list, Handler handler, Context context) {
		this.list = (List<T>) list;
		this.handler = handler;
		this.context = context;
	}

	public List<T> getList() {
		return list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
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

	public void setList(List<T> list) {
		this.list = list;
	}

	public void clearList() {
		if (list != null) {
			list.clear();
			notifyDataSetChanged();
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
}
