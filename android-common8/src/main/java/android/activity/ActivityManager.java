package android.activity;

import java.util.Stack;

import android.app.Activity;

public class ActivityManager {
	private Stack<Activity> activityStack;
	private static ActivityManager instance;
	/**启动第一个Activity*/
	private Activity loadActivity;

	public static ActivityManager getActivityManager() {
		if (instance == null) {
			instance = new ActivityManager();
		}
		return instance;
	}

	public Stack<Activity> getActivityStack() {
		return activityStack;
	}

	public void setActivityStack(Stack<Activity> activityStack) {
		this.activityStack = activityStack;
	}

	/**
	 * 指定activity退出栈
	 * @author :Atar
	 * @createTime:2011-9-5下午3:31:00
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param activity
	 * @description:
	 */
	public void popActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * 得到栈顶activity
	 * @author :Atar
	 * @createTime:2011-9-5下午3:32:20
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:
	 */
	public Activity currentActivity() {
		Activity activity = null;
		if (activityStack != null && !activityStack.empty())
			activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 将当前activity压入栈
	 * @author :Atar
	 * @createTime:2011-9-5下午3:33:13
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param activity
	 * @description:
	 */
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 除指定的activity其余退出栈
	 * @author :Atar
	 * @createTime:2011-9-5下午3:30:06
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param cls
	 * @description:
	 */
	public <A extends Activity> void popAllActivityExceptOne(A activity) {
		while (true) {
			Activity mActivity = currentActivity();
			if (activity == null || (activityStack != null && activityStack.size() > 0 && activityStack.size() == 1 && activityStack.get(0).getClass().equals(activity.getClass()))) {
				break;
			}
			if (mActivity.getClass().equals(activity.getClass())) {

			} else {
				if (!activity.isFinishing()) {
					activity.finish();
				}
				popActivity(mActivity);
			}
		}
	}

	/**
	 * 退出所有activity
	 * @author :Atar
	 * @createTime:2011-9-5下午3:28:39
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public void popAllActivity() {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (!activity.isFinishing()) {
				activity.finish();
			}
			popActivity(activity);
		}
	}

	/**
	 * 得到前一个activity
	 * @author :Atar
	 * @createTime:2014-9-5下午2:18:44
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param position
	 * @return
	 * @description:
	 */
	public Activity getPreviousActivity() {
		if (activityStack == null || activityStack.size() == 0 || activityStack.size() < 2) {
			return null;
		}
		return activityStack.get(activityStack.size() - 2);
	}

	/**
	 * 倒数第几个Activity
	 * @author :Atar
	 * @createTime:2016-6-16下午7:30:58
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param lastPosition
	 * @return
	 * @description:
	 */
	public Activity getActivity(int lastPosition) {
		if (activityStack == null || activityStack.size() == 0 || activityStack.size() < lastPosition) {
			return null;
		}
		return activityStack.get(activityStack.size() - lastPosition);
	}

	/**
	 * 得到指定activity
	 * @author :Atar
	 * @createTime:2014-9-5下午2:21:04
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param cls
	 * @return
	 * @description:
	 */
	@SuppressWarnings("unchecked")
	public <A extends Activity> A getActivity(Class<A> cls) {
		A a = null;
		for (int i = activityStack.size() - 1; i >= 0; i--) {
			if (activityStack.get(i).getClass().equals(cls)) {
				a = (A) activityStack.get(i);
				break;
			}
		}
		return a;
	}

	/**
	 * 得到指定activity 倒数第几个 从倒数第0开始算
	 * @author :Atar
	 * @createTime:2014-9-5下午2:21:04
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param cls
	 * @return
	 * @description:
	 */
	@SuppressWarnings("unchecked")
	public <A extends Activity> A getActivity(Class<A> cls, int lastPosition) {
		A a = null;
		int tempLastPosition = 0;
		for (int i = activityStack.size() - 1; i >= 0; i--) {
			if (activityStack.get(i).getClass().equals(cls)) {
				if (tempLastPosition == lastPosition) {
					a = (A) activityStack.get(i);
					break;
				}
				tempLastPosition++;
			}
		}
		return a;
	}

	/**
	 * 关闭指定activity同时也将此activity退出栈
	 * @author :Atar
	 * @createTime:2014-9-5下午2:32:12
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param cls
	 * @description: 从最后算起，若有相同的关闭最后一个 用i--
	 */
	public <A extends Activity> void finishActivity(Class<A> cls) {
		for (int i = activityStack.size() - 1; i >= 0; i--) {
			if (activityStack.get(i).getClass().equals(cls)) {
				activityStack.get(i).finish();
				activityStack.remove(i);
				break;
			}
		}
	}

	/**
	 * 关闭两个Activity
	 * @author :Atar
	 * @createTime:2015-11-9下午4:58:15
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param clsA
	 * @param clsB
	 * @description:
	 */
	public <A extends Activity, B extends Activity> void finishActivity2(Class<A> clsA, Class<B> clsB) {
		int flag = 0;
		for (int i = activityStack.size() - 1; i >= 0; i--) {
			if (flag == 2) {
				break;
			}
			if (activityStack.get(i).getClass().equals(clsA)) {
				activityStack.get(i).finish();
				activityStack.remove(i);
				flag++;
			} else if (activityStack.get(i).getClass().equals(clsB)) {
				activityStack.get(i).finish();
				activityStack.remove(i);
				flag++;
			}
		}
	}

	/**
	 * 关闭当前activity同时退出栈
	 * @author :Atar
	 * @createTime:2014-9-5下午3:27:55
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public void finishCurrentActivity() {
		if (activityStack != null && activityStack.size() > 0) {
			activityStack.get(activityStack.size() - 1).finish();
			activityStack.remove(activityStack.size() - 1);
		}
	}

	/**
	 * 设置第一个启动的Activity
	 * @author :Atar
	 * @createTime:2016-8-18下午2:30:32
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param loadActivity
	 * @description: 如果第一次启动后就关闭 如果在使用中如调用 popAllActivity 会出现黑一下屏幕 情形在浏览用户已经加载了好多个activity下再次登录 先关掉只前的所有activity（popAllActivity（）方法）
	 */
	public void setLoadActivity(Activity loadActivity) {
		this.loadActivity = loadActivity;
	}

	/**
	 * 退出程序
	 * @author :Atar
	 * @createTime:2016-8-18下午2:30:03
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public void exitApplication() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				if (loadActivity != null) {
					loadActivity.finish();
				}
				popAllActivity();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
			}
		}.start();
	}
}
