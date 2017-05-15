package tony.com.googleplay.ui.holder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
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
 * 应用详情页的应用模块
 * Created by Administrator on 2017/5/5.
 */

public class DetailAppHolder extends BaseHolder<AppInfo> {

    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.rb_star)
    RatingBar mRbStar;
    @BindView(R.id.tv_download_num)
    TextView mTvDownloadNum;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_size)
    TextView mTvSize;

    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.layout_detail_appinfo);

        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
//        System.out.println("DetailAppHolder================");
//        System.out.println(data);
        if(data!=null){
            OkGo.get(HttpHelper.URL + "image?name=" + data.iconUrl)
                    .tag(this)//
                    .execute(new BitmapCallback() {

                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {
                            // bitmap 即为返回的图片数据
                            mIvIcon.setImageBitmap(bitmap);
                        }
                    });
            mTvName.setText(data.name);
            mRbStar.setRating(data.stars);
            mTvDownloadNum.setText("下载量:"+data.downloadNum);
            mTvVersion.setText("版本:"+data.version);
            mTvDate.setText(data.date);
            mTvSize.setText(android.text.format.Formatter.formatFileSize(UIUtils.getContext(),data.size));
        }


    }
}
