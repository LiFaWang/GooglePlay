package tony.com.googleplay.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import tony.com.googleplay.R;
import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.domain.DownloadInfo;
import tony.com.googleplay.manager.DownloadManager;
import tony.com.googleplay.ui.widget.ProgressHorizontal;
import tony.com.googleplay.utils.UIUtils;


/**
 *
 * Created by Administrator on 2017/5/9.
 */

public class DetailDownLoadHolder extends BaseHolder<AppInfo>implements DownloadManager.DownloadObserver,View.OnClickListener {

    private ProgressHorizontal mProgressHorizontal;
    private DownloadManager mManager;
    private float progress;//下载进度
    private int currentState;//下载状态
    private FrameLayout mFlprgress;
    private Button  btnDownload;

    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.layout_detail_download);
        btnDownload= (Button) view.findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);
        //帧布局添加进度条的自定义控件
        mFlprgress = (FrameLayout) view.findViewById(R.id.fl_progress);
        mFlprgress.setOnClickListener(this);

        mProgressHorizontal = new ProgressHorizontal(UIUtils.getContext());

        mProgressHorizontal.setProgressTextColor(Color.WHITE);
        mProgressHorizontal.setProgressTextSize(UIUtils.dip2px(18));
        mProgressHorizontal.setBackgroundResource(R.drawable.progress_bg);//进度条背景
        mProgressHorizontal.setProgressResource(R.drawable.progress_normal);//进度图片
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mFlprgress.addView(mProgressHorizontal,params);
        mManager = DownloadManager.getInstance();
        mManager.registerObserver(this);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        //如果之前下载过就需要把当时的状态展示出来
        DownloadInfo info = mManager.getDownloadInfo(data);
        if (info!=null){
            progress=info.getProgress();
            currentState=info.currentState;
        }else {
            progress=0;
            currentState=DownloadManager.STATE_UNDO;
        }
//根据下载的进度和状态刷新界面
        refreshUI(progress,currentState);
    }
//根据状态和进度刷新界面
    private void refreshUI(float progress, int currentState) {
        this.progress=progress;
        this.currentState=currentState;
        switch (currentState) {
            case DownloadManager.STATE_UNDO://未下载
                mFlprgress.setVisibility(View.GONE);
                btnDownload.setText("下载");
                break;
            case DownloadManager.STATE_WAITING://等待中
                mFlprgress.setVisibility(View.GONE);
                btnDownload.setText("等待中...");
                break;
            case DownloadManager.STATE_DOWNLOADING://正在下载
                mFlprgress.setVisibility(View.VISIBLE);
                mProgressHorizontal.setProgress(progress);
                mProgressHorizontal.setCenterText("");
                break;
            case DownloadManager.STATE_PAUSE://暂停
                mFlprgress.setVisibility(View.VISIBLE);
                mProgressHorizontal.setProgress(progress);
                mProgressHorizontal.setCenterText("暂停");
                break;
            case DownloadManager.STATE_ERROR://下载失败
                mFlprgress.setVisibility(View.GONE);
                btnDownload.setText("下载失败");
                break;
            case DownloadManager.STATE_SUCCESS://正在下载
                mFlprgress.setVisibility(View.GONE);
                btnDownload.setText("安装");
                break;

            default:
                break;
        }

    }

    @Override
    public void onDownloadStateChanged(DownloadInfo info) {
        refreshUIOnMainThread(info);
    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo info) {
        refreshUIOnMainThread(info);

    }
    private void refreshUIOnMainThread(final DownloadInfo info){
        AppInfo appInfo = getData();
        if (appInfo.id.equals(info.id)){//只有下载对象是当前应用，才刷新界面
            UIUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    refreshUI(info.getProgress(),info.currentState);
                }
            });


        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:


            case R.id.fl_progress:
                if(currentState==DownloadManager.STATE_UNDO||currentState==DownloadManager.STATE_PAUSE||currentState==DownloadManager.STATE_ERROR){
                    mManager.download(getData());
                }else if (currentState==DownloadManager.STATE_WAITING||currentState==DownloadManager.STATE_DOWNLOADING){
                    mManager.pause(getData());

                }else if(currentState==DownloadManager.STATE_SUCCESS) {
                    mManager.install(getData());
                }
                break;
            default:
                break;
        }

    }
}
