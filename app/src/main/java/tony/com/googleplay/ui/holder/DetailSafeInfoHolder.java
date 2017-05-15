package tony.com.googleplay.ui.holder;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import tony.com.googleplay.R;
import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.http.HttpHelper;
import tony.com.googleplay.utils.UIUtils;

/**
 *
 * Created by Administrator on 2017/5/5.
 */

public class DetailSafeInfoHolder extends BaseHolder<AppInfo> {
    @BindViews({R.id.iv_safe1, R.id.iv_safe2, R.id.iv_safe3, R.id.iv_safe4})
    public List<ImageView> mSafeImageList;
    @BindViews({R.id.iv_des1, R.id.iv_des2, R.id.iv_des3, R.id.iv_des4})
    public List<ImageView> mDesImageList;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.rl_des_root)
    RelativeLayout mRlDesRoot;

    @BindViews({R.id.tv_des1, R.id.tv_des2, R.id.tv_des3, R.id.tv_des4})
    public List<TextView> mTvDesList;
    @BindViews({R.id.ll_des1, R.id.ll_des2, R.id.ll_des3, R.id.ll_des4})
    public List<LinearLayout> mLlDesList;
    @BindView(R.id.ll_des_root)
    LinearLayout mLlDesRoot;
    private int mMeasuredHeight;
    private boolean isOpen;
    private ValueAnimator mAnimator;


    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.layout_detail_safeinfo);

        ButterKnife.bind(this, view);
        //隐藏安全信息的布局（将该布局高度设置为0）
        ViewGroup.LayoutParams layoutParams = mLlDesRoot.getLayoutParams();
        layoutParams.height = 0;//将高度设置为0
        mLlDesRoot.setLayoutParams(layoutParams);

        return view;
    }

    @Override
    public void refreshView(AppInfo data) {

//        System.out.println("DetailSafeHolder=======================");
//        System.out.println(data);
        if(data!=null){
            for (int i = 0; i < 4; i++) {
//            ArrayList<AppInfo.SafeInfo> safe = data.safe;

                if (i < data.safe.size()) {
                    AppInfo.SafeInfo info = data.safe.get(i);
//                System.out.println(info);
                    final int finalI = i;

                    OkGo.get(HttpHelper.URL
                            + "image?name=" + info.safeUrl).tag(this).execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {

                            mSafeImageList.get(finalI).setImageBitmap(bitmap);



                        }
                    });
                    OkGo.get(HttpHelper.URL
                            + "image?name=" + info.safeDesUrl).tag(this).execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {

                            mDesImageList.get(finalI).setImageBitmap(bitmap);
                        }
                    });
                    mTvDesList.get(i).setText(info.safeDes);
                } else {
                    //隐藏控件布局
                    mSafeImageList.get(i).setVisibility(View.GONE);
                    mLlDesList.get(i).setVisibility(View.GONE);

                }
            }
        }

        //测量安全信息布局的高度
        mLlDesRoot.measure(0, 0);
        //测量后的高度
        mMeasuredHeight = mLlDesRoot.getMeasuredHeight();
        initAnimator();
    }
    private void initAnimator() {
        mAnimator = ValueAnimator.ofInt(0, mMeasuredHeight);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLlDesRoot.getLayoutParams().height= (int) animation.getAnimatedValue();
                mLlDesRoot.requestLayout();
                mIvArrow.setRotation(animation.getAnimatedFraction()*180);
            }
        });

        mAnimator.setDuration(200);
    }

    //点击收起/展开安全布局
    @OnClick({R.id.rl_des_root,R.id.ll_des_root})
    public void onViewClicked() {
        toggle();
    }
    /**
     * 展开/收起安全布局
     */
    private void toggle() {
        if (!isOpen) {
            mAnimator.start();
//            RotateAnimation rotateAnimation = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//            rotateAnimation.setDuration(200);
//            rotateAnimation.setFillAfter(true);
//            mIvArrow.setAnimation(rotateAnimation);
//            rotateAnimation.start();

        } else {
            mAnimator.reverse();
//            RotateAnimation rotateAnimation = new RotateAnimation(180,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//            rotateAnimation.setDuration(200);
//            rotateAnimation.setFillAfter(true);
//            mIvArrow.setAnimation(rotateAnimation);
//            rotateAnimation.start();
        }
//        mIvArrow.clearAnimation();
        isOpen = !isOpen;
    }
}



