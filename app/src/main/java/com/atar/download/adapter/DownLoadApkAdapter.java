package com.atar.download.adapter;

import android.adapter.CommonAdapter;
import android.app.Activity;
import android.appconfig.AppConfigModel;
import android.appconfig.moudle.DownloadApkBean;
import android.download.DownLoadFileManager;
import android.interfaces.HandlerListener;
import android.os.Message;
import android.utils.MDPassword;
import android.utils.ShowLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atar.download.DownLoadListActivity;
import com.atar.download.MainActivity;
import com.atar.download.widgets.DownloadProgressButton;
import com.atar.downloadapp.R;

import java.io.File;
import java.util.List;

/**
 * @author：atar
 * @date: 2019/10/12
 * @description:
 */
public class DownLoadApkAdapter extends CommonAdapter<DownloadApkBean> {

    /**
     * SD卡目录 下载 资源文件 皮肤资源
     */
    private String strDownloadDir;

    private String TAG = DownLoadApkAdapter.class.getSimpleName();

    public DownLoadApkAdapter(List<?> list) {
        super(list);
    }

    public void setStrDownloadDir(String strDownloadDir) {
        this.strDownloadDir = strDownloadDir;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        setContext(parent.getContext());
        final ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_download_apk_item, null);
            mViewHolder.mDownloadProgressButton = (DownloadProgressButton) convertView.findViewById(R.id.down_btn);
            mViewHolder.txt_re_download = (TextView) convertView.findViewById(R.id.txt_re_download);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mDownloadProgressButton.setBackgroundColor(parent.getContext().getResources().getColor(R.color.colorPrimary));
        mViewHolder.mDownloadProgressButton.setTextColor(parent.getContext().getResources().getColor(R.color.color_black));
        mViewHolder.mDownloadProgressButton.setTextCoverColor(parent.getContext().getResources().getColor(R.color.color_ffffffff));
        mViewHolder.mDownloadProgressButton.setBackgroundSecondColor(parent.getContext().getResources().getColor(R.color.color_ffffffff));

        final DownloadApkBean info = getItem(position);
        if (info != null) {
            try {
                mViewHolder.mDownloadProgressButton.setButtonRadius(0);
                mViewHolder.mDownloadProgressButton.setEnablePause(true);
                mViewHolder.mDownloadProgressButton.setBackgroundColor(parent.getContext().getResources().getColor(R.color.colorPrimary));
                mViewHolder.mDownloadProgressButton.setProgress(info.getProgress());
                mViewHolder.txt_re_download.setBackgroundColor(parent.getContext().getResources().getColor(R.color.colorPrimary));
            } catch (Exception e) {

            }
            mViewHolder.mDownloadProgressButton.setCurrentText(info.getDownloadName());
            if (info.getProgress() >= 100) {
                mViewHolder.mDownloadProgressButton.finish();
                mViewHolder.mDownloadProgressButton.setCurrentText(info.getLoadName());
                mViewHolder.txt_re_download.setVisibility(View.VISIBLE);
                mViewHolder.txt_re_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File oldDownloadFile = new File(strDownloadDir + MDPassword.getPassword32(info.getDownloadFileName()));
                        oldDownloadFile.delete();
                        mViewHolder.mDownloadProgressButton.reset();
                        mViewHolder.mDownloadProgressButton.setCurrentText(info.getDownloadName());
                    }
                });
            } else {
                mViewHolder.txt_re_download.setVisibility(View.GONE);
            }
            mViewHolder.mDownloadProgressButton.setOnDownLoadClickListener(new DownloadProgressButton.OnDownLoadClickListener() {

                @Override
                public void clickResume(View v) {
                    clickDownload(v);
                }

                @Override
                public void clickPause(View v) {
                    DownLoadFileManager.getInstance().pauseDownload(position);
                }

                @Override
                public void clickFinish(View v) {
                    File file = new File(strDownloadDir + MDPassword.getPassword32(info.getDownloadFileName()));
                    if (file.exists()) {
                        ((DownLoadListActivity) parent.getContext()).install(file);
                    }
                }

                @Override
                public void clickDownload(View v) {
                    String ip = AppConfigModel.getInstance().getString(MainActivity.IP_KEY, "10.208.24.208:8080");
                    DownLoadFileManager.getInstance().downLoad((Activity) parent.getContext(), mViewHolder.mHandlerListener, position, "http://" + ip + info.getUrl(), MDPassword.getPassword32(info.getDownloadFileName()),
                            strDownloadDir);
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        TextView txt_re_download;
        DownloadProgressButton mDownloadProgressButton;
        HandlerListener mHandlerListener = new HandlerListener() {
            @Override
            public void onHandlerData(Message msg) {
                switch (msg.what) {
                    case android.download.DownLoadFileBean.DOWLOAD_FLAG_FAIL:
                        ShowLog.i(TAG, msg.arg2 + "---" + "下载失败");
                        break;
                    case android.download.DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                        getList().get(msg.arg2).setProgress(100);
                        mDownloadProgressButton.setProgress(100);
                        txt_re_download.setVisibility(View.VISIBLE);
                        mDownloadProgressButton.finish();
                        mDownloadProgressButton.setCurrentText(getList().get(msg.arg2).getLoadName());
                        File file = new File(strDownloadDir + MDPassword.getPassword32(getList().get(msg.arg2).getDownloadFileName()));
                        if (file.exists()) {
                            ((DownLoadListActivity) getContext()).install(file);
                        }
                        ShowLog.i(TAG, msg.arg2 + "---" + "下载成功");
                        break;
                    case android.download.DownLoadFileBean.DOWLOAD_FLAG_ING:
                        int progress = (Integer) msg.obj;
                        mDownloadProgressButton.setProgress(progress);
                        break;
                }
            }
        };
    }
}
