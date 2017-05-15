package tony.com.googleplay.ui.holder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;
import tony.com.googleplay.R;
import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.http.HttpHelper;
import tony.com.googleplay.utils.UIUtils;

/**
 * Created by Administrator on 2017/5/8.
 */

public class DetailPicInfoHolder extends BaseHolder<AppInfo> {

    @BindViews({R.id.iv_pic1,R.id.iv_pic2,R.id.iv_pic3,R.id.iv_pic4,R.id.iv_pic5})
    List<ImageView> mImageViewList;
    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.layout_detail_picinfo);
//        ImageView imageView= new ImageView(UIUtils.getContext());

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {

        ArrayList<String> screen = data.screen;

        for (int i = 0; i < 5; i++) {
            if (i<screen.size()){
                final int finalI = i;
                OkGo.get(HttpHelper.URL
                        + "image?name=" + screen.get(i))
                        .tag(this)
                        .execute(new BitmapCallback() {
                            @Override
                            public void onSuccess(Bitmap bitmap, Call call, Response response) {
                                mImageViewList.get(finalI).setImageBitmap(bitmap);
                            }
                        });
            }else {
                mImageViewList.get(i).setVisibility(View.GONE);
            }

        }

    }


}
