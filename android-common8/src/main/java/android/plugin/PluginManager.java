package android.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.reflection.ThreadPoolTool;

import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * ****************************************************************************************************************************************************************************
 * 插件管理
 *
 * @author:Atar
 * @createTime: 2018/8/27 上午11:10
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description :
 * **************************************************************************************************************************************************************************
 */
public class PluginManager {
    private static PluginManager instance = new PluginManager();
    private Context context;
    private DexClassLoader pluginDexClassLoader;
    private Resources pluginResources;
    private PackageInfo pluginPackageArchiveInfo;

    public static PluginManager getInstance() {
        return instance;
    }

//    public PackageInfo getPluginPackageArchiveInfo() {
//        return pluginPackageArchiveInfo;
//    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public DexClassLoader getPluginDexClassLoader() {
        return pluginDexClassLoader;
    }

    public Resources getPluginResources() {
        return pluginResources;
    }

    public void loadApk(final String apkPath, final PluginListener mPluginListener) {
        ThreadPoolTool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (context == null) {
                    return;
                }
                if (pluginDexClassLoader != null && pluginResources != null && pluginPackageArchiveInfo != null) {
                    //已经加载过
                    if (mPluginListener != null) {
                        mPluginListener.success(pluginResources, pluginPackageArchiveInfo);
                    }
                    return;
                }
                try {
                    pluginDexClassLoader = new DexClassLoader(apkPath, context.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath(), null, context.getClassLoader());
                    pluginPackageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);

                    AssetManager assets = AssetManager.class.newInstance();
                    Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
                    addAssetPath.invoke(assets, apkPath);
                    pluginResources = new Resources(assets, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
                    if (mPluginListener != null) {
                        mPluginListener.success(pluginResources, pluginPackageArchiveInfo);
                    }
                } catch (Exception e) {
                    if (mPluginListener != null) {
                        mPluginListener.fail();
                    }
                }
            }
        });
    }

    public boolean exists(String className) {
        int flag = 0;
        if (pluginPackageArchiveInfo != null && pluginPackageArchiveInfo.activities != null && pluginPackageArchiveInfo.activities.length > 0) {
            for (int i = 0; i < pluginPackageArchiveInfo.activities.length; i++) {
                if (className.equals(pluginPackageArchiveInfo.activities[i].name)) {
                    flag = 1;
                    break;
                }
            }
        }
        return flag == 1;
    }
}
