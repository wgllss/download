package com.atar.download;

import android.Manifest;
import android.activity.ActivityManager;
import android.appconfig.AppConfigDownloadManager;
import android.appconfig.AppConfigModel;
import android.appconfig.moudle.ConfigJson;
import android.appconfig.moudle.UpdateApkInfo;
import android.content.pm.PackageManager;
import android.download.DownLoadFileBean;
import android.download.DownLoadFileManager;
import android.interfaces.HandlerListener;
import android.interfaces.NetWorkCallListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.reflection.ErrorMsgEnum;
import android.reflection.NetWorkMsg;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.update.UpdateApkTools;
import android.utils.GsonUtils;
import android.utils.MDPassword;
import android.utils.PackageUtil;
import android.utils.ShowLog;
import android.view.View;
import android.widget.CommonToast;
import android.widget.EditText;

import com.atar.download.widgets.DownloadProgressButton;
import com.atar.downloadapp.R;

import java.io.File;


public class MainActivity extends AppCompatActivity implements DownloadProgressButton.OnDownLoadClickListener, HandlerListener, NetWorkCallListener, View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();

    private final int INSTALL_PACKAGES_REQUESTCODE = 12334;

    private EditText edt_ip;
    private EditText edt_thread_num, edt_thread_num2;
    private EditText edit_ip2;
    private DownloadProgressButton btn_down;

    public static String IP_KEY = "IP_KEY";

    //获取配置标识
    private final int what = 100023;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManager.getActivityManager().pushActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_ip = findViewById(R.id.edt_ip);
        edt_thread_num = findViewById(R.id.edt_thread_num);
        edt_thread_num2 = findViewById(R.id.edt_thread_num2);
        edit_ip2 = findViewById(R.id.edit_ip2);
        btn_down = findViewById(R.id.btn_down);

        findViewById(R.id.btn_save_ip).setOnClickListener(this);

        btn_down.setOnDownLoadClickListener(this);
        btn_down.setButtonRadius(0);
        btn_down.setEnablePause(true);
        btn_down.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btn_down.setTextColor(getResources().getColor(R.color.color_black));
        btn_down.setTextCoverColor(getResources().getColor(R.color.color_ffffffff));
        btn_down.setBackgroundSecondColor(getResources().getColor(R.color.color_ffffffff));
        btn_down.setCurrentText("下载");

        edit_ip2.setText(AppConfigModel.getInstance().getString(IP_KEY, "10.208.24.208:8080"));
    }

    @Override
    public void clickDownload(View v) {
        int threadNum = 1;
        String strthreadNum = edt_thread_num.getText().toString();
        if (!TextUtils.isEmpty(strthreadNum)) {
            try {
                threadNum = Integer.valueOf(strthreadNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fileUrl = "";
        switch (v.getId()) {
            case R.id.btn_down:
                fileUrl = edt_ip.getText().toString();
                if (TextUtils.isEmpty(fileUrl)) {
                    CommonToast.show("下载地址为空");
                    return;
                }
                if (!fileUrl.contains("http")) {
                    fileUrl = "http://" + fileUrl;
                }
                DownLoadFileManager.getInstance().downLoad(this, this, 1, fileUrl, threadNum, true, UpdateApkTools.fileName, UpdateApkTools.strDownloadDir);
                break;
//            case R.id.btn_down1:
//                fileUrl = edit_ip2.getText().toString();
//                if (TextUtils.isEmpty(fileUrl)) {
//                    CommonToast.show("下载地址为空");
//                    return;
//                }
//                fileUrl = "http://" + fileUrl + "/assets/config/android_config_moudle.txt";
//                AppConfigDownloadManager.getInstance().getServerJson(fileUrl, what, ErrorMsgEnum.NetWorkMsgWhithToast, this, this);
//                return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_ip:
                String fileUrl = edit_ip2.getText().toString();
                if (TextUtils.isEmpty(fileUrl)) {
                    CommonToast.show("下载地址为空");
                    return;
                }
                fileUrl = "http://" + fileUrl + "/assets/config/android_config_moudle.txt";
                AppConfigDownloadManager.getInstance().getServerJson(fileUrl, what, ErrorMsgEnum.NetWorkMsgWhithToast, this, this);
                break;
        }
    }

    @Override
    public void clickPause(View v) {
        switch (v.getId()) {
            case R.id.btn_down:
                DownLoadFileManager.getInstance().pauseDownload(1);
                break;
            case R.id.btn_save_ip:
                break;
//            case R.id.btn_down1:
//                DownLoadFileManager.getInstance().pauseDownload(2);
//                break;
        }
    }

    @Override
    public void clickResume(View v) {
        clickDownload(v);
    }

    @Override
    public void clickFinish(View v) {
        clickDownload(v);
    }

    @Override
    public void onHandlerData(Message msg) {
        switch (msg.what) {
            case DownLoadFileBean.DOWLOAD_FLAG_FAIL:
                ShowLog.i(TAG, msg.arg2 + ":下载失败");
                break;
            case DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                File downloadFile = null;
                switch (msg.arg2) {
                    case 1:
                        btn_down.setProgress(100);
                        btn_down.finish();
                        btn_down.setCurrentText("正在安装...");
                        downloadFile = new File(UpdateApkTools.strDownloadDir + UpdateApkTools.fileName);
                        break;
                    case 2:
//                        btn_down1.setProgress(100);
//                        btn_down1.finish();
//                        btn_down1.setCurrentText("正在安装...");
//                        downloadFile = new File(UpdateApkTools.strDownloadDir + MDPassword.getPassword32(UpdateApkTools.fileName));
                        break;
                }
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
                ShowLog.i(TAG, msg.arg2 + ":下载成功");
                break;
            case DownLoadFileBean.DOWLOAD_FLAG_ING:
                int progress = (Integer) msg.obj;
                switch (msg.arg2) {
                    case 1:
                        btn_down.setProgress(progress);
                        btn_down.setVisibility(View.VISIBLE);
                        break;
                    case 2:
//                        btn_down1.setProgress(progress);
//                        btn_down1.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case UpdateApkTools.UPDATA_IS_NEW_VERSION:
                if (msg.obj != null) {
                    UpdateApkInfo updateApkInfo = (UpdateApkInfo) msg.obj;
                    if (updateApkInfo != null) {
                        String fileUrl = updateApkInfo.getUrl();
                        String newVersion = updateApkInfo.getVersion();
                        int which = 2;
                        if (!TextUtils.isEmpty(fileUrl)) {
                            int threadNum = 1;
                            String strthreadNum = edt_thread_num2.getText().toString();
                            if (!TextUtils.isEmpty(strthreadNum)) {
                                try {
                                    threadNum = Integer.valueOf(strthreadNum);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            AppConfigDownloadManager.getInstance().downLoadAppConfigFile(this, this, newVersion, "1.0.00", which, fileUrl, threadNum, true, MDPassword.getPassword32(UpdateApkTools.fileName), UpdateApkTools.strDownloadDir);
                        }
                    }
                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            clickFinish(null);
        } else {
            CommonToast.show("安装权限申请失败");
        }
    }

    private void installApk(File downloadFile) {
        PackageUtil.install(this, downloadFile, "com.atar.downloadapp.fileprovider", 0);
    }


    @Override
    public void NetWorkCall(NetWorkMsg msg) {
        switch (msg.what) {
            case what:
                if (msg != null && msg.obj != null) {
                    ConfigJson configJson = (ConfigJson) msg.obj;
                    if (configJson != null) {
                        if (configJson.getDownloadApkList() != null && configJson.getDownloadApkList().size() > 0) {
                            AppConfigModel.getInstance().putString(DownLoadListActivity.KEY, GsonUtils.beanToJson(configJson.getDownloadApkList()), true);
                        }
                        String ip = edit_ip2.getText().toString();
                        AppConfigModel.getInstance().putString(IP_KEY, ip, true);
                        DownLoadListActivity.startDownLoadListActivity(this);
//                        ConfigUtils.getInstance().setConfigResult(this, configJson, this);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityManager.getActivityManager().exitApplication();
    }
}
