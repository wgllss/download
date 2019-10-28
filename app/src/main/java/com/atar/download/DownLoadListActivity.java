package com.atar.download;

import android.Manifest;
import android.app.Activity;
import android.appconfig.AppConfigModel;
import android.appconfig.moudle.DownloadApkBean;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.download.DownLoadFileManager;
import android.interfaces.HandlerListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.utils.MDPassword;
import android.utils.PackageUtil;
import android.widget.CommonToast;
import android.widget.GridView;
import android.widget.ListView;

import com.atar.download.adapter.DownLoadApkAdapter;
import com.atar.downloadapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DownLoadListActivity extends AppCompatActivity implements HandlerListener {

    public static final String KEY = "downloadapklist";
    private final int INSTALL_PACKAGES_REQUESTCODE = 12334;

    private GridView listview;

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
        SD_PATH = SD_PATH + MDPassword.getPassword32(getPackageName()) + "/";
        downLoadApkAdapter.setStrDownloadDir(SD_PATH);


        String json = AppConfigModel.getInstance().getString(KEY, "");
        if (!TextUtils.isEmpty(json)) {
            List<DownloadApkBean> list = new Gson().fromJson(json, new TypeToken<List<DownloadApkBean>>() {
            }.getType());
            if (list != null && list.size() > 0) {
                this.list.addAll(list);
                for (int i = 0; i < list.size(); i++) {
                    String fileUrl = AppConfigModel.getInstance().getString(MainActivity.IP_KEY, "10.208.24.208:8080");
                    if (!list.get(i).getUrl().contains("http://")) {
                        fileUrl = "http://" + fileUrl + list.get(i).getUrl();
                    } else {
                        fileUrl = list.get(i).getUrl();
                    }
                    DownLoadFileManager.getInstance().initTempFilePercent(i, this, fileUrl, MDPassword.getPassword32(list.get(i).getDownloadFileName()), SD_PATH);
                }
            }
        }
        listview.setAdapter(downLoadApkAdapter);
    }

    @Override
    public void onHandlerData(Message msg) {
        switch (msg.what) {
            case android.download.DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                list.get(msg.arg2).setProgress(100);
                break;
            case android.download.DownLoadFileBean.DOWLOAD_FLAG_ING:
                int progress = (Integer) msg.obj;
                list.get(msg.arg2).setProgress(progress);
                break;
        }
        if (downLoadApkAdapter != null) {
            downLoadApkAdapter.notifyDataSetChanged();
        }
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
