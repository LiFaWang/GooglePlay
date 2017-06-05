package tony.com.googleplay.ui.holder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;
import tony.com.googleplay.R;
import tony.com.googleplay.domain.SubjectInfo;
import tony.com.googleplay.http.HttpHelper;
import tony.com.googleplay.utils.UIUtils;

/**
 *
 * Created by Administrator on 2017/4/12.
 */

public class SubHolder extends BaseHolder<SubjectInfo> {
    @BindView(R.id.iv_pic)
    ImageView mIvPic;
    @BindView(R.id.tv_des)
    TextView mTvDes;

    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.list_item_subject);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void refreshView(SubjectInfo data) {
        if (data != null) {
            mTvDes.setText(data.des);
            OkGo.get(HttpHelper.URL + "image?name=" + data.url)
                    .tag(this)
                    .cacheKey("subject")
                    .cacheMode(CacheMode.DEFAULT)
                    .execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {
                            mIvPic.setImageBitmap(bitmap);
                        }
                    });
        }

    }
}
