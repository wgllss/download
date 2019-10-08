package android.fragment;

import android.appconfig.AppConfigSetting;
import android.content.res.Resources;
import android.enums.SkinMode;
import android.skin.SkinResourcesManager;
import android.skin.SkinResourcesManager.loadSkinCallBack;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 
 *****************************************************************************************************************************************************************************
 * 暂时先实现了换皮肤功能 
 * @author :Atar
 * @createTime:2014-8-18上午11:01:11
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public abstract class CommonFragment extends Fragment {

	@Override
	public void onStart() {
		super.onStart();
		loadSkin(getCurrentSkinType());
	}

	/**
	 * 得到当前皮肤模式
	 * @author :Atar
	 * @createTime:2015-3-16下午4:31:06
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:
	 */
	protected int getCurrentSkinType() {
		return AppConfigSetting.getInstance().getInt(SkinMode.SKIN_MODE_KEY, 0);
	}

	/**
	 * 改变皮肤
	 * @author :Atar
	 * @createTime:2017-9-18下午1:34:13
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public void loadSkin(final int skinType) {
		if (SkinResourcesManager.isLoadApkSkin) {
			if (SkinResourcesManager.getInstance(getActivity()).getResources() != null) {
				ChangeSkin(getCurrentSkinType());
			} else {
				SkinResourcesManager.getInstance(getActivity()).loadSkinResources(new loadSkinCallBack() {

					@Override
					public void loadSkinSuccess(Resources mResources) {
						ChangeSkin(skinType);
					}
				});
			}
		} else {
			ChangeSkin(skinType);
		}
	}

	public void ChangeSkin(int skinType) {
	}

	/**
	 * 设置fragment
	 * @author :Atar
	 * @createTime:2014-9-22下午5:51:54
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param replaceLayoutID
	 * @param f
	 * @description:
	 */
	protected void setFragment(int replaceLayoutID, Fragment f) {
		FragmentManager fm = getChildFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		f.setUserVisibleHint(true);// 高手都是这样的
		ft.replace(replaceLayoutID, f);
		// ft.commit();
		ft.commitAllowingStateLoss();
	}

	protected void switchFragment(int replaceLayoutID, Fragment from, Fragment to) {
		if (to != null) {
			FragmentManager fm = getChildFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			if (from != null) {
				if (!from.isAdded()) {
					ft.add(replaceLayoutID, from);
				}
				if (from.isVisible()) {
					ft.hide(from);
				}
			}
			to.setUserVisibleHint(true);
			if (!to.isAdded()) {
				ft.add(replaceLayoutID, to);
			} else {
				ft.show(to);
			}
			ft.commitAllowingStateLoss();
		}
	}
}
