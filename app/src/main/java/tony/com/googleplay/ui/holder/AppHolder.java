package tony.com.googleplay.ui.holder;

import android.graphics.Bitmap;
import android.text.format.Formatter;
import android.view.View;
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
import tony.com.googleplay.http.HttpHelper;
import tony.com.googleplay.utils.UIUtils;

/**
 * 应用页
 * Created by Administrator on 2017/4/9.
 */

public class AppHolder extends BaseHolder<AppInfo> {

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


    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.list_item_home);
        ButterKnife.bind(this, view);
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
                    .cacheKey("app").cacheMode(CacheMode.DEFAULT)
                    .execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {
                            mIvItem.setImageBitmap(bitmap);
                        }
                    });
        }
    }


    }

