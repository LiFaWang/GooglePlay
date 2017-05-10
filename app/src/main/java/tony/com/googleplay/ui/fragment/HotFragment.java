package tony.com.googleplay.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import tony.com.googleplay.http.protocol.HotProtocol;
import tony.com.googleplay.ui.widget.FlowLayout;
import tony.com.googleplay.ui.widget.LoadingPage;
import tony.com.googleplay.utils.DrawableUtils;
import tony.com.googleplay.utils.UIUtils;

/**
 * 排行
 * Created by Administrator on 2017/4/3.
 */

public class HotFragment extends BaseFragment {

    private ArrayList<String> mData;

    @Override
    public View onCreateSuccessView() {
        ScrollView scrollView=new ScrollView(UIUtils.getContext());
        // 设置内边距
        int padding = UIUtils.dip2px(10);
        scrollView.setPadding(padding,padding,padding,padding);
        FlowLayout flowLayout=new FlowLayout(UIUtils.getContext());

        flowLayout.setHorizontalSpacing(UIUtils.dip2px(6));
        flowLayout.setVerticalSpacing(UIUtils.dip2px(8));
        Random random = new Random();
        // 添加所有的子元素
        for (int i = 0; i < mData.size(); i++) {
            TextView view=new TextView(UIUtils.getContext());
            final String keyWord = mData.get(i);

            view.setText(keyWord);
            view.setTextColor(Color.WHITE);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            view.setGravity(Gravity.CENTER);
            view.setPadding(padding,padding,padding,padding);
            // 随机颜色
            // 0-255
            int r = 30 + random.nextInt(210);// 30-239
            int g = 30 + random.nextInt(210);// 30-239
            int b = 30 + random.nextInt(210);// 30-239
            int color = 0xffcecece;// 按下后偏白的背景色
            // 生成状态选择器
            StateListDrawable selector = DrawableUtils.getSelector(
                    Color.rgb(r, g, b), color, UIUtils.dip2px(6));
            view.setBackgroundDrawable(selector);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), keyWord, Toast.LENGTH_SHORT).show();
                }
            });
            flowLayout.addView(view);
        }
        scrollView.addView(flowLayout);

        return scrollView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        HotProtocol protocol=new HotProtocol();
        mData = protocol.getData(0);
        return check(mData);
    }
}
