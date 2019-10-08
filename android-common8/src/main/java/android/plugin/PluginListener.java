package android.plugin;

import android.content.pm.PackageInfo;
import android.content.res.Resources;

/**
 * ****************************************************************************************************************************************************************************
 * 插件加载监听
 *
 * @author:Atar
 * @createTime: 2018/8/27 上午11:28
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description :
 * **************************************************************************************************************************************************************************
 */
public interface PluginListener {

    void success(Resources pluginResources, PackageInfo pluginPackageArchiveInfo);

    void fail();
}
