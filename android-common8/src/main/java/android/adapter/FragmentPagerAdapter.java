package android.adapter;//package android.adapter;
//
//import java.util.List;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//
//public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
//	private List<Fragment> fragmentsList;
//	private List<String> titleList;
//
//	public FragmentPagerAdapter(FragmentManager fm) {
//		super(fm);
//	}
//
//	public FragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
//		super(fm);
//		this.fragmentsList = fragments;
//	}
//
//	public FragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments,
//			List<String> titleList) {
//		super(fm);
//		this.fragmentsList = fragments;
//		this.titleList = titleList;
//	}
//
//	@Override
//	public int getCount() {
//		return fragmentsList == null ? 0 : fragmentsList.size();
//	}
//
//	@Override
//	public Fragment getItem(int arg0) {
//		return fragmentsList.get(arg0);
//	}
//
//	@Override
//	public int getItemPosition(Object object) {
//		return super.getItemPosition(object);
//	}
//
//	@Override
//	public CharSequence getPageTitle(int position) {
//		return (titleList != null && titleList.size() > position) ? titleList
//				.get(position) : "";
//	}
//}
