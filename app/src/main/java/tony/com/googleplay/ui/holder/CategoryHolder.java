package tony.com.googleplay.ui.holder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import tony.com.googleplay.R;
import tony.com.googleplay.domain.CategoryInfo;
import tony.com.googleplay.http.HttpHelper;
import tony.com.googleplay.utils.UIUtils;

/**
 * 分类模块-普通类型
 * Created by Administrator on 2017/4/14.
 */

public class CategoryHolder extends BaseHolder<CategoryInfo> {
    @BindView(R.id.iv_icon1)
    ImageView mIvIcon1;
    @BindView(R.id.tv_name1)
    TextView mTvName1;
    @BindView(R.id.ll_grid1)
    LinearLayout mLlGrid1;
    @BindView(R.id.iv_icon2)
    ImageView mIvIcon2;
    @BindView(R.id.tv_name2)
    TextView mTvName2;
    @BindView(R.id.ll_grid2)
    LinearLayout mLlGrid2;
    @BindView(R.id.iv_icon3)
    ImageView mIvIcon3;
    @BindView(R.id.tv_name3)
    TextView mTvName3;
    @BindView(R.id.ll_grid3)
    LinearLayout mLlGrid3;
    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.list_item_category);
        ButterKnife.bind(this,view);
        onViewClicked(view);
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {

        if(data!=null){
            OkGo.get(HttpHelper.URL + "image?name=" + data.url1)
                    .tag(this)
                    .cacheKey("url1").cacheMode(CacheMode.DEFAULT)
                    .execute(new BitmapCallback() {

                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {
                            mIvIcon1.setImageBitmap(bitmap);
                        }
                    });
            OkGo.get(HttpHelper.URL + "image?name=" + data.url2)
                    .tag(this)
                    .cacheKey("url2").cacheMode(CacheMode.DEFAULT)
                    .execute(new BitmapCallback() {

                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {
                            mIvIcon2.setImageBitmap(bitmap);
                        }
                    });
            OkGo.get(HttpHelper.URL + "image?name=" + data.url3)
                    .tag(this)
                    .cacheKey("url3").cacheMode(CacheMode.DEFAULT)
                    .execute(new BitmapCallback() {

                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {
                            mIvIcon3.setImageBitmap(bitmap);
                        }
                    });
            mTvName1.setText(data.name1);
            mTvName2.setText(data.name2);
            mTvName3.setText(data.name3);
        }
    }

    @OnClick({R.id.ll_grid1, R.id.ll_grid2, R.id.ll_grid3})
    public void onViewClicked(View view) {
        CategoryInfo info=getData();
        switch (view.getId()) {
            case R.id.ll_grid1:
                Toast.makeText(UIUtils.getContext(), info.name1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_grid2:
                Toast.makeText(UIUtils.getContext(), info.name2, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_grid3:
                Toast.makeText(UIUtils.getContext(), info.name3, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
