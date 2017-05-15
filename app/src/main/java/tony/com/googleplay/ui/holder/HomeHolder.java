package tony.com.googleplay.ui.holder;

import android.graphics.Bitmap;
import android.text.format.Formatter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;
import tony.com.googleplay.R;
import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.domain.DownloadInfo;
import tony.com.googleplay.http.HttpHelper;
import tony.com.googleplay.manager.DownloadManager;
import tony.com.googleplay.ui.widget.ProgressArc;
import tony.com.googleplay.utils.UIUtils;

/**
 * 首页
 * Created by Administrator on 2017/4/9.
 */

public class HomeHolder extends BaseHolder<AppInfo> implements DownloadManager.DownloadObserver,View.OnClickListener {

    @BindView(R.id.iv_item)
    ImageView mIvItem;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.rb_star)
    RatingBar mRbStar;
    @BindView(R.id.tv_size)
    TextView mTvSize;
    @BindView(R.id.tv_download)
    TextView mTvDownload;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.fl_progress)
    FrameLayout mFlProgress;
    private ProgressArc mProgressArc;
    private DownloadManager mManager;
    private float progress;//下载进度
    private int currentState;//下载状态


    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.list_item_home);
        ButterKnife.bind(this, view);
        //添加圆形的进度条
        mProgressArc = new ProgressArc(UIUtils.getContext());
        mProgressArc.setArcDiameter(UIUtils.dip2px(26));//设置直径
        mProgressArc.setProgressColor(UIUtils.getColor(R.color.progress));//设置布局参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(UIUtils.dip2px(27), UIUtils.dip2px(27));
        mFlProgress.addView(mProgressArc, params);
        mFlProgress.setOnClickListener(this);
        mManager = DownloadManager.getInstance();
        mManager.registerObserver(this);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        if (data != null) {
            mTvName.setText(data.name);
            mRbStar.setRating(data.stars);
            mTvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
            mTvDesc.setText(data.des);
            OkGo.get(HttpHelper.URL + "image?name=" + data.iconUrl)
                    .tag(this)
                    .cacheKey("home").cacheMode(CacheMode.DEFAULT)
                    .execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {
                            mIvItem.setImageBitmap(bitmap);
                        }
                    });

        //如果之前下载过就需要把当时的状态展示出来
        DownloadInfo info = mManager.getDownloadInfo(data);
        if (info!=null){
            progress=info.getProgress();
            currentState=info.currentState;
        }else {
            progress=0;
            currentState=DownloadManager.STATE_UNDO;
        }
        }
        //根据下载的进度和状态刷新界面
        refreshUI(progress, currentState, data.id);
    }

    //根据状态和进度刷新界面
    private void refreshUI(float progress, int currentState, String id) {
        //确保因为listView的重用机制导致滑动时，更新进度错乱
        if (!getData().id.equals(id)){
            return;
        }
        this.progress = progress;
        this.currentState = currentState;
        switch (currentState) {
            case DownloadManager.STATE_UNDO://未下载
                mTvDownload.setText("下载");
                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//不显示进度
                mProgressArc.setBackgroundResource(R.drawable.ic_download);
                break;
            case DownloadManager.STATE_WAITING://等待中
                mTvDownload.setText("等待");
                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);//等待
                mProgressArc.setBackgroundResource(R.drawable.ic_download);
                break;
            case DownloadManager.STATE_DOWNLOADING://正在下载
                mTvDownload.setText(((int)(progress*100)+"%"));
                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);//不正在下载进度
                mProgressArc.setBackgroundResource(R.drawable.ic_pause);
                mProgressArc.setProgress(progress,true);
                break;
            case DownloadManager.STATE_PAUSE://暂停
                mTvDownload.setText("暂停");
                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//不显示进度
                mProgressArc.setBackgroundResource(R.drawable.ic_resume);//继续下载
                break;
            case DownloadManager.STATE_ERROR://下载失败
                mTvDownload.setText("失败");
                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//不显示进度
                mProgressArc.setBackgroundResource(R.drawable.ic_redownload);//重新下载
                break;
            case DownloadManager.STATE_SUCCESS://下载成功
                mTvDownload.setText("安装");
                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//不显示进度
                mProgressArc.setBackgroundResource(R.drawable.ic_install);//安装
                break;

            default:
                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    @Override
    public void onDownloadStateChanged(DownloadInfo info) {
        refreshUIOnMainThread(info);

    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo info) {
        refreshUIOnMainThread(info);

    }

    private void refreshUIOnMainThread(final DownloadInfo info) {
        AppInfo appInfo = getData();
        if (appInfo.id.equals(info.id)) {//只有下载对象是当前应用，才刷新界面
            UIUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    refreshUI(info.getProgress(), info.currentState,info.id);
                }
            });
        }
    }
}
