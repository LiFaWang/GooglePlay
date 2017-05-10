package tony.com.googleplay.ui.holder;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import tony.com.googleplay.R;
import tony.com.googleplay.http.protocol.HttpHelper;
import tony.com.googleplay.utils.UIUtils;

/**
 *
 * Created by Administrator on 2017/4/17.
 */

public class HeadViewHolder extends BaseHolder<ArrayList<String>> {


    private ViewPager mViewPager;
    private LinearLayout mIndicator;
    private int prePosition;

    @Override
    public View initView() {
        //初始化跟布局
        RelativeLayout root = new RelativeLayout(UIUtils.getContext());
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
                UIUtils.getDimen(R.dimen.list_header_height));
        root.setLayoutParams(params);
        //给跟布局添加ViewPager
        mViewPager = new ViewPager(UIUtils.getContext());
        RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        root.addView(mViewPager, vpParams);//给ViewPager,宽高填充父窗体
        //初始化小圆点指示器布局
        mIndicator = new LinearLayout(UIUtils.getContext());
        mIndicator.setOrientation(LinearLayout.HORIZONTAL);//水平方向
        RelativeLayout.LayoutParams mLlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//下方
        mLlParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT );//右方
        int padding = UIUtils.dip2px(5);
        mIndicator.setPadding(padding,padding,padding,padding);
        root.addView(mIndicator, mLlParams);

        return root;
    }

    @Override
    public void refreshView(ArrayList<String> data) {
        mViewPager.setAdapter(new HeaderAdapter());
        mViewPager.setCurrentItem(data.size()*10000);

        for (int i = 0; i < data.size(); i++) {
            ImageView ivIndicator = new ImageView(UIUtils.getContext());
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i>0){
                params.leftMargin=UIUtils.dip2px(3);
                ivIndicator.setImageResource(R.drawable.indicator_normal);
            }else {
                ivIndicator.setImageResource(R.drawable.indicator_selected);
            }
            ivIndicator.setLayoutParams(params);

            mIndicator.addView(ivIndicator);
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position=position%getData().size();
                ImageView childAt = (ImageView) mIndicator.getChildAt(position);
                childAt.setImageResource(R.drawable.indicator_selected);
                ImageView preChildAt= (ImageView) mIndicator.getChildAt(prePosition);
                preChildAt.setImageResource(R.drawable.indicator_normal);
                prePosition=position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //自动轮播效果
        new Task().start();

    }
    class Task implements Runnable{
        //启动轮播动画
        public void start(){
            //移除所有消息
            UIUtils.getHandler().removeCallbacksAndMessages(null);
            UIUtils.getHandler().postDelayed(this,3000);
        }


        @Override
        public void run() {
            //每个3秒走run方法
            //  跳到下个页面
            int currentItem = mViewPager.getCurrentItem();
            currentItem++;
            mViewPager.setCurrentItem(currentItem);
            //继续发消息
            UIUtils.getHandler().postDelayed(this,3000);

        }
    }
    private class HeaderAdapter extends PagerAdapter {
        @Override
        public int getCount() {
//            return getData().size();
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position=position%getData().size();
            //给viewPager添加ImageView
            final ImageView view = new ImageView(UIUtils.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String url = getData().get(position);
            OkGo.get(HttpHelper.URL + "image?name=" + url)
                    .tag(this)
                   .cacheMode(CacheMode.DEFAULT)
                    .execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Bitmap bitmap, Call call, Response response) {
                            view.setImageBitmap(bitmap);
                        }
                    });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
