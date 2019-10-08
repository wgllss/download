package com.atar.download;

import android.Manifest;
import android.activity.ActivityManager;
import android.content.pm.PackageManager;
import android.download.DownLoadFileBean;
import android.download.DownLoadFileManager;
import android.interfaces.HandlerListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.update.UpdateApkTools;
import android.utils.MDPassword;
import android.utils.PackageUtil;
import android.utils.ShowLog;
import android.view.View;
import android.widget.CommonToast;
import android.widget.EditText;

import com.atar.download.widgets.DownloadProgressButton;
import com.atar.downloadapp.R;

import java.io.File;


public class MainActivity extends AppCompatActivity implements DownloadProgressButton.OnDownLoadClickListener, HandlerListener {

    private String TAG = MainActivity.class.getSimpleName();

    private final int INSTALL_PACKAGES_REQUESTCODE = 12334;

    private EditText edt_ip;
    private EditText edt_thread_num;
    private DownloadProgressButton btn_down;
    private String fileUrl;

    private int threadNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManager.getActivityManager().pushActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_ip = findViewById(R.id.edt_ip);
        edt_thread_num = findViewById(R.id.edt_thread_num);
        btn_down = findViewById(R.id.btn_down);

        btn_down.setOnDownLoadClickListener(this);
        btn_down.setButtonRadius(0);
        btn_down.setEnablePause(true);
        btn_down.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btn_down.setTextColor(getResources().getColor(R.color.color_black));
        btn_down.setTextCoverColor(getResources().getColor(R.color.color_ffffffff));
        btn_down.setBackgroundSecondColor(getResources().getColor(R.color.color_ffffffff));
        btn_down.setCurrentText("下载");
    }

    @Override
    public void clickDownload(View v) {
        fileUrl = edt_ip.getText().toString();
        fileUrl = "http://" + fileUrl;
        if (TextUtils.isEmpty(fileUrl)) {
            CommonToast.show("下载地址为空");
            return;
        }
        String strthreadNum = edt_thread_num.getText().toString();
        if (TextUtils.isEmpty(strthreadNum)) {
            threadNum = 1;
        }
        threadNum = Integer.valueOf(strthreadNum);
        DownLoadFileManager.getInstance().downLoad(this, this, 98999, fileUrl, threadNum, true, MDPassword.getPassword32(UpdateApkTools.fileName), UpdateApkTools.strDownloadDir);
    }

    @Override
    public void clickPause(View v) {

    }

    @Override
    public void clickResume(View v) {

    }

    @Override
    public void clickFinish(View v) {

    }

    @Override
    public void onHandlerData(Message msg) {
        switch (msg.what) {
            case DownLoadFileBean.DOWLOAD_FLAG_FAIL:
                ShowLog.i(TAG, fileUrl + ":下载失败");
                break;
            case DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                btn_down.setProgress(100);
                btn_down.finish();
                btn_down.setCurrentText("正在安装...");

                final File downloadFile = new File(UpdateApkTools.strDownloadDir + MDPassword.getPassword32(UpdateApkTools.fileName));
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
                ShowLog.i(TAG, fileUrl + ":下载成功");
                break;
            case DownLoadFileBean.DOWLOAD_FLAG_ING:
                int progress = (Integer) msg.obj;
                btn_down.setProgress(progress);
                btn_down.setVisibility(View.VISIBLE);
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
    public void onBackPressed() {
        ActivityManager.getActivityManager().exitApplication();
    }
}
