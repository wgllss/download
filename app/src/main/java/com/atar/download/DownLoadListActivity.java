package com.atar.download;

import android.Manifest;
import android.app.Activity;
import android.appconfig.AppConfigModel;
import android.appconfig.moudle.DownloadApkBean;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.utils.MDPassword;
import android.utils.PackageUtil;
import android.widget.CommonToast;
import android.widget.ListView;

import com.atar.download.adapter.DownLoadApkAdapter;
import com.atar.downloadapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DownLoadListActivity extends AppCompatActivity {

    public static final String KEY = "downloadapklist";
    private final int INSTALL_PACKAGES_REQUESTCODE = 12334;

    private ListView listview;

    private List<DownloadApkBean> list = new ArrayList<DownloadApkBean>();
    private DownLoadApkAdapter downLoadApkAdapter = new DownLoadApkAdapter(list);

    /**
     * SD卡目录 下载 资源文件
     */
    private String SD_PATH = Environment.getExternalStorageDirectory() + "/.cache/.apk/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_list);
        listview = findViewById(R.id.listview);

        String json = AppConfigModel.getInstance().getString(KEY, "");
        if (!TextUtils.isEmpty(json)) {
            List<DownloadApkBean> list = new Gson().fromJson(json, new TypeToken<List<DownloadApkBean>>() {
            }.getType());
            if (list != null && list.size() > 0) {
                this.list.addAll(list);
            }
        }
        SD_PATH = SD_PATH + MDPassword.getPassword32(getPackageName()) + "/";
        downLoadApkAdapter.setStrDownloadDir(SD_PATH);
        listview.setAdapter(downLoadApkAdapter);
    }

    public void install(File downloadFile) {
        if (downloadFile != null && downloadFile.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean b = getPackageManager().canRequestPackageInstalls();
                if (b) {
                    installApk(downloadFile);//安装应用的逻辑(写自己的就可以)
                } else {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
                }
            } else {
                installApk(downloadFile);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            CommonToast.show("安装权限申请失败");
        }
    }

    private void installApk(File downloadFile) {
        PackageUtil.install(this, downloadFile, "com.atar.downloadapp.fileprovider", 0);
    }

    public static void startDownLoadListActivity(Context context) {
        context.startActivity(new Intent(context, DownLoadListActivity.class));
    }
}
