package tony.com.googleplay.ui.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import tony.com.googleplay.http.protocol.RecommendProtocol;
import tony.com.googleplay.ui.widget.LoadingPage;
import tony.com.googleplay.ui.widget.fly.StellarMap;
import tony.com.googleplay.utils.UIUtils;

/**
 * 推荐
 * Created by Administrator on 2017/4/3.
 */

public class RecommendFragment extends BaseFragment {

    private ArrayList<String> mData;

    @Override
    public View onCreateSuccessView() {
        StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        stellarMap.setAdapter(new RecommendAdapter());
        int padding=UIUtils.dip2px(10);
        //设置内边距
        stellarMap.setInnerPadding(padding,padding,padding,padding);
        // 设置随机密度, 生成6列9行的随机布局
        stellarMap.setRegularity(6,9);
        stellarMap.setGroup(0,true);// 默认显示第一组
        return stellarMap;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        RecommendProtocol protocol = new RecommendProtocol();
        mData = protocol.getData(0);
        return check(mData);
    }

    class RecommendAdapter implements StellarMap.Adapter {
        // 组个数(多少批数据)
        @Override
        public int getGroupCount() {
            return 2;
        }

        // 当前组有多少元素
        @Override
        public int getCount(int group) {
            int count = mData.size() / getGroupCount();// 计算每组平均item个数
            if (group == getGroupCount() - 1) {
                // 将除不尽的余数追加在最后一组
                count += mData.size() % getGroupCount();
            }
            return count;
        }

        // 初始化每个元素布局
        @Override
        public View getView(int group, int position, View convertView) {
            // 第一组: 0,1,2,3...15
            // 第二组: 0,1,2,3..15 -> 16, 17....
            position+=getCount(group-1)*group;// 更新position, 追加上几组的数量
            String keyWord = mData.get(position);
            TextView view=new TextView(UIUtils.getContext());
            view.setText(keyWord);
            //// 随机大小
            Random random=new Random();
            int size=16+random.nextInt(10);//16-25
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);//设置文字大小，单位sp
            //随机颜色
            int r=30+random.nextInt(210);
            int g=30+random.nextInt(210);
            int b=30+random.nextInt(210);
            view.setTextColor(Color.rgb(r,g,b));
            return view;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            if (isZoomIn) {
                // 向下滑, 上一页数据
                if(group>0){
                    group--;
                }else {
                    group=getGroupCount()-1;// 如果滑到头,展示最后一页数据
                }
            } else {
                // 向上滑, 下一页数据
                if (group<getGroupCount()-1){
                    group++;
                }else {
                    group=0;// 如果滑到头,展示第一页数据
                }

            }
            return group;
        }
    }
}
