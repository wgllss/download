package android.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.appconfig.AppConfigSetting;
import android.common.CommonHandler;
import android.content.Context;
import android.content.res.Resources;
import android.enums.SkinMode;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.interfaces.HandlerListener;
import android.interfaces.OnOpenDrawerCompleteListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.skin.SkinResourcesManager;
import android.skin.SkinResourcesManager.loadSkinCallBack;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.utils.FileUtils;
import android.utils.ShowLog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.CommonToast;
import android.widget.DrawerBack;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.IOException;
import java.util.List;

/**
 *
 *****************************************************************************************************************************************************************************
 *
 * @author :Atar
 * @createTime:2014-8-18上午10:53:08
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description: https://github.com/wgllss/Common8.git
 *****************************************************************************************************************************************************************************
 */
@SuppressLint({ "ClickableViewAccessibility", "NewApi" })
public abstract class CommonActivity extends FragmentActivity {
	private static String TAG = CommonActivity.class.getSimpleName();
	/* ---加载图片方法1 start */
	public ImageLoader imageLoader = ImageLoader.getInstance();
	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";
	public AbsListView listView;
	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;

	/* ---加载图片方法1 end */
	/***********************************************************************************************************/
	/* 从左向右滑动代替返回已封装 ,onCreate 实例化一个，其它只需要继承此类，不需要的调用setOnDrawerBackEnabled（）方法 */
	private DrawerBack mDrawerBack;

	/* 播放mp3 or amr 语音 *****************start*********** */
	/** 正在播放 msg.arg1>0为播放中ing msg.arg1=0为播放开始*/
	public static final int AUDIO_PLAYING = 0x1216;
	/** 播放停止 msg.arg1>0为中途停止播放 msg.arg1=0为自动播放结束*/
	public static final int AUDIO_STOP = 0x1217;
	protected static HandlerListener mHandlerStatusListener;
	private MediaPlayer mMediaPlayer;
	protected static AnimationDrawable animationDrawable;
	protected static String strAudioPath;
	protected static ImageView mImageView;
	protected static int mStopImgResID = 0;
	/* 播放mp3 or amr 语音 *****************end*********** */
	private boolean isActive;// 应该程序是否在前台

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActivityManager.getActivityManager().pushActivity(this);
		mDrawerBack = new DrawerBack(this);
	}

	/**
	 * 监听皮肤必须在所在view创建完成，固onCreate之后
	 * @author :Atar
	 * @createTime:2014-8-18上午11:08:10
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	@Override
	protected void onStart() {
		super.onStart();
		loadSkin(getCurrentSkinType());
	}

	@Override
	protected void onResume() {
		super.onResume();
		applyScrollListener();
		if (!isActive) {
			isActive = true;
			OnRunBackground(!isActive);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		CommonToast.dissMissToast();
		onPauseAudio();
	}

	@Override
	protected void onStop() {
		super.onStop();
		CommonToast.dissMissToast();
		onPauseAudio();
		if (!isAppOnForeground()) {
			isActive = false;
			OnRunBackground(!isActive);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CommonToast.dissMissToast();
		ActivityManager.getActivityManager().popActivity(this);
		onPauseAudio();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
		pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
	}

	protected void applyScrollListener() {
		if (listView != null) {
			listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
		outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
	}

	/**
	 *
	 * 打开或者关闭从左向右滑动代替返回
	 * @author :Atar
	 * @createTime:2014-6-6上午10:10:07
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param enable
	 * @description:
	 */
	public void setOnDrawerBackEnabled(boolean enable) {
		if (mDrawerBack != null) {
			mDrawerBack.setOnDrawerBackEnabled(enable);
		}
	}

	/**
	 * 监听DrawerCallbacks
	 * @author :Atar
	 * @createTime:2014-11-4下午8:19:55
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param mOnOpenDrawerCompleteListener
	 * @description:
	 */
	public void setOnOpenDrawerCompleteListener(OnOpenDrawerCompleteListener mOnOpenDrawerCompleteListener) {
		if (mDrawerBack != null) {
			mDrawerBack.setOnOpenDrawerCompleteListener(mOnOpenDrawerCompleteListener);
		}
	}

	/**
	 * 主动调用滑动返回
	 * @author :Atar
	 * @createTime:2015-12-16上午10:38:16
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public void setBackFinishActivity() {
		if (mDrawerBack != null) {
			mDrawerBack.openDrawer();
		}
	}

	public void setOnBackFacusView(View view) {
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						setOnDrawerBackEnabled(false);
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_OUTSIDE:
					case MotionEvent.ACTION_UP:
						setOnDrawerBackEnabled(true);
						break;
					default:
						break;
				}
				return false;
			}
		});
	}

	/**
	 * 用滑动返回
	 * @author :Atar
	 * @createTime:2014-6-20下午4:00:42
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public void DrawerBackFinish() {
		if (mDrawerBack != null) {
			mDrawerBack.openDrawer();
		} else {
			finish();
		}
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
			if (SkinResourcesManager.getInstance(this).getResources() != null) {
				ChangeSkin(getCurrentSkinType());
				ShowLog.i(TAG, "加载已加载好的皮肤");
			} else {
				SkinResourcesManager.getInstance(this).loadSkinResources(new loadSkinCallBack() {

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

	/**
	 * 抽象方法监听改变皮肤
	 * @author :Atar
	 * @createTime:2014-8-18上午10:43:57
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param skinType
	 * @description:
	 */
	public void ChangeSkin(int skinType) {

	}

	/**
	 * 获取当前皮肤类型
	 * @author :Atar
	 * @createTime:2014-8-19下午5:51:18
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:
	 */
	public int getCurrentSkinType() {
		return AppConfigSetting.getInstance().getInt(SkinMode.SKIN_MODE_KEY, 0);
	}

	/**
	 * 加载图片，可本地，网络，assets,缓存，圆角，gif格式,AbsListView加载图片时所用
	 * @author :Atar
	 * @createTime:2014-6-25上午11:14:33
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param imgUrl
	 * @param mImageView
	 * @param defaultImg
	 * @param animateFirstListener
	 * @param rounde:圆角度数
	 * @description:
	 */
	@SuppressWarnings("deprecation")
	public void LoadImageView(String imgUrl, ImageView mImageView, int defaultImg, ImageLoadingListener animateFirstListener, int rounde) {
		if (imgUrl == null || "".equals(imgUrl)) {
			mImageView.setImageResource(defaultImg);
			return;
		}
		if (imgUrl.contains("?")) {
			imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf("?"));
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(defaultImg).showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg).cacheInMemory().cacheOnDisc()
				.extraForDownloader(1).displayer(new RoundedBitmapDisplayer(rounde)).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.displayImage(imgUrl, mImageView, options, animateFirstListener);
	}

	/**
	 * 加载图片，可本地，网络，assets,缓存，圆角，gif格式
	 * @author :Atar
	 * @createTime:2014-6-25上午11:14:33
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param imgUrl
	 * @param mImageView
	 * @param defaultImg
	 * @param rounde:圆角度数
	 * @description:
	 */
	@SuppressWarnings("deprecation")
	public void LoadImageView(String imgUrl, ImageView mImageView, int defaultImg, int rounde) {
		if (imgUrl == null || "".equals(imgUrl)) {
			mImageView.setImageResource(defaultImg);
			return;
		}
		if (imgUrl.contains("?")) {
			imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf("?"));
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(defaultImg).showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg).cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(rounde)).build();
		imageLoader.displayImage(imgUrl, mImageView, options);
	}

	/**
	 * 加载图片可缩放 可本地，网络，assets,不缓存，圆角，gif格式 带缓存
	 * @author :Atar
	 * @createTime:2014-12-11下午10:25:27
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param imgUrl
	 * @param mImageView
	 * @description:
	 */
	@SuppressWarnings("deprecation")
	public void LoadImageView(String imgUrl, ImageView mImageView, int defaultImgResId) {
		if (mImageView == null || imgUrl == null || imageLoader == null) {
			return;
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(defaultImgResId).showImageForEmptyUri(defaultImgResId).showImageOnFail(defaultImgResId).cacheInMemory()
				.cacheOnDisc().build();
		imageLoader.displayImage(imgUrl, mImageView, options);
	}

	/**
	 * 加载图片，可本地，网络，assets,不缓存，圆角，gif格式 不带缓存
	 * @author :Atar
	 * @createTime:2014-6-25上午11:14:33
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param imgUrl
	 * @param mImageView
	 * @param defaultImg
	 * @description:
	 */
	@SuppressWarnings("deprecation")
	public void LoadImageViewWhitOutCache(String imgUrl, ImageView mImageView, int defaultImg) {
		if (mImageView == null || imgUrl == null || imgUrl.length() == 0 || imageLoader == null) {
			return;
		}
		mImageView.setImageResource(defaultImg);
		if (imageLoader.getDiscCache() != null && imageLoader.getDiscCache().get(imgUrl) != null && imageLoader.getDiscCache().get(imgUrl).getPath() != null
				&& imageLoader.getDiscCache().get(imgUrl).getPath().length() > 0 && FileUtils.exists(imageLoader.getDiscCache().get(imgUrl).getPath())) {
			FileUtils.deleteFile(imageLoader.getDiscCache().get(imgUrl).getPath());
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(defaultImg).showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg).cacheInMemory().cacheOnDisc().build();
		imageLoader.displayImage(imgUrl, mImageView, options);
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
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		f.setUserVisibleHint(true);
		ft.replace(replaceLayoutID, f);
		// ft.commit();
		ft.commitAllowingStateLoss();
	}

	/**
	 * 是否显示fragment
	 * @author :Atar
	 * @createTime:2015-6-23上午9:56:22
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param mFragment
	 * @description:
	 */
	public void isShowFragment(Fragment mFragment, boolean isShow) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (mFragment != null) {
			if (isShow) {
				ft.show(mFragment);
			} else {
				ft.hide(mFragment);
			}
			ft.commitAllowingStateLoss();
		}
	}

	/**
	 * 切换fragment时显示or隐藏
	 * @author :Atar
	 * @createTime:2015-6-30下午3:07:17
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param from
	 * @param to
	 * @description:
	 */
	public void switchFragment(int replaceLayoutID, Fragment from, Fragment to) {
		if (to != null) {
			FragmentManager fm = getSupportFragmentManager();
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

	/**
	 * 停止语音播放
	 * @author :Atar
	 * @createTime:2014-12-2下午2:44:12
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	private void onPauseAudio() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			if (mHandlerStatusListener != null) {
				CommonHandler.getInstatnce().handerMessage(mHandlerStatusListener, AUDIO_STOP, 0, 0, null);
			}
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
			if (animationDrawable != null) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						animationDrawable.stop();
						animationDrawable.selectDrawable(0);
					}
				});
			}
		}
	}

	/**
	 * 播放语音 mp3，amr格式
	 * @author :Atar
	 * @createTime:2014-10-8下午4:35:57
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param localPath
	 * @description:
	 */
	@SuppressLint("NewApi")
	public void playAudio(String localPath, AnimationDrawable mAnimationDrawable, ImageView imageView, int stopImgResID, final HandlerListener mHandlerListener) {
		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
		}
		if (localPath == null || localPath.length() == 0) {
			return;
		}
		if (mImageView != null && mStopImgResID != 0) {
			mImageView.setImageResource(mStopImgResID);
		}
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			if (mHandlerStatusListener != null) {
				CommonHandler.getInstatnce().handerMessage(mHandlerStatusListener, AUDIO_STOP, 1, 0, null);// 中途停止播放
			}
			mMediaPlayer.release();
			mMediaPlayer = null;
			if (mImageView != null && mStopImgResID != 0) {
				mImageView.setImageResource(mStopImgResID);
			}
			if (strAudioPath != null && strAudioPath.length() > 0 && strAudioPath.equals(localPath)) {
				// 正在播放又再次点击就停止播放
				return;
			}
		}
		try {
			if (mMediaPlayer == null) {
				mMediaPlayer = new MediaPlayer();
			}
			mMediaPlayer.setDataSource(localPath);
			mMediaPlayer.prepare();
			strAudioPath = localPath;
			mImageView = imageView;
			mStopImgResID = stopImgResID;
			mHandlerStatusListener = mHandlerListener;
			mMediaPlayer.start();
			if (mHandlerStatusListener != null) {
				CommonHandler.getInstatnce().handerMessage(mHandlerStatusListener, AUDIO_PLAYING, 0, 0, null);
			}
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					// if (mOnCompletionListener != null) {
					// mOnCompletionListener.onCompletion(mp);
					// }
					if (mHandlerStatusListener != null) {
						CommonHandler.getInstatnce().handerMessage(mHandlerStatusListener, AUDIO_STOP, 0, 0, null);
					}
					mMediaPlayer.stop();
					mMediaPlayer.release();
					mMediaPlayer = null;
					if (animationDrawable != null) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								animationDrawable.stop();
								animationDrawable.selectDrawable(0);
							}
						});
					}
				}
			});
			animationDrawable = mAnimationDrawable;
			if (animationDrawable != null && mImageView != null) {
				mImageView.setImageDrawable(animationDrawable);
				animationDrawable.start();
			}
		} catch (IOException e) {
			if (mHandlerStatusListener != null) {
				CommonHandler.getInstatnce().handerMessage(mHandlerStatusListener, AUDIO_STOP, 1, 0, null);// 中途停止播放
			}
			mMediaPlayer.release();
			mMediaPlayer = null;// 必须设为空，不然只执行一次播放
			if (animationDrawable != null) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						animationDrawable.stop();
						animationDrawable.selectDrawable(0);
					}
				});
			}
			e.printStackTrace();
		}
	}

	/**
	 * 监听是否在后台运行
	 * @author :Atar
	 * @createTime:2015-11-9下午5:23:08
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param isOnBackGround
	 * @description:
	 */
	protected void OnRunBackground(boolean isOnBackGround) {

	}

	/**
	 * 应用是否在前台
	 * @author :Atar
	 * @createTime:2015-11-9下午5:13:48
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:
	 */
	protected boolean isAppOnForeground() {
		android.app.ActivityManager activityManager = (android.app.ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 播放网页中的语音
	 * @author :Atar
	 * @createTime:2015-1-22上午10:33:31
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param localPath
	 * @description:
	 */
	public void playAudio(String localPath, HandlerListener mHandlerListener) {
		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
		}
		if (localPath == null || localPath.length() == 0) {
			return;
		}
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
			if (mHandlerStatusListener != null) {
				CommonHandler.getInstatnce().handerMessage(mHandlerStatusListener, AUDIO_STOP, 0, 1, null);
			}
			if (strAudioPath != null && strAudioPath.length() > 0 && strAudioPath.equals(localPath)) {
				// 正在播放又再次点击就停止播放
				return;
			}
		}
		try {
			if (mMediaPlayer == null) {
				mMediaPlayer = new MediaPlayer();
			}
			mMediaPlayer.setDataSource(localPath);
			mMediaPlayer.prepare();
			strAudioPath = localPath;
			mHandlerStatusListener = mHandlerListener;
			if (mHandlerStatusListener != null) {
				CommonHandler.getInstatnce().handerMessage(mHandlerStatusListener, AUDIO_PLAYING, 0, 1, null);
			}
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					if (mHandlerStatusListener != null) {
						CommonHandler.getInstatnce().handerMessage(mHandlerStatusListener, AUDIO_STOP, 0, 0, null);
					}
					mMediaPlayer.stop();
					mMediaPlayer.release();
					mMediaPlayer = null;
				}
			});
		} catch (IOException e) {
			if (mHandlerStatusListener != null) {
				CommonHandler.getInstatnce().handerMessage(mHandlerStatusListener, AUDIO_STOP, 0, 0, null);
			}
			mMediaPlayer.release();
			mMediaPlayer = null;// 必须设为空，不然只执行一次播放
			e.printStackTrace();
		}
	}
}
