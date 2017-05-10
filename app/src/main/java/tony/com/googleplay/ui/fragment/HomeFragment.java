package tony.com.googleplay.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.http.protocol.HomeProtocol;
import tony.com.googleplay.ui.activity.HomeDetailActivity;
import tony.com.googleplay.ui.adapter.MyBaseAdapter;
import tony.com.googleplay.ui.holder.BaseHolder;
import tony.com.googleplay.ui.holder.HeadViewHolder;
import tony.com.googleplay.ui.holder.HomeHolder;
import tony.com.googleplay.ui.widget.LoadingPage;
import tony.com.googleplay.ui.widget.MyListView;
import tony.com.googleplay.utils.UIUtils;

/**
 * 首页
 * Created by Administrator on 2017/4/3.
 */

public class HomeFragment extends BaseFragment {

    private ArrayList<AppInfo> mData;
    private ArrayList<String> mPictures;

    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UIUtils.getContext());

        HeadViewHolder headViewHolder=new HeadViewHolder();

        view.addHeaderView(headViewHolder.getRootView());
        //设置头布局的数据
        headViewHolder.setData(mPictures);
        //初始化数据
        view.setAdapter(new HomeAdapter(mData));
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo info = mData.get(position-1);//减掉头布局

                Intent intent=new Intent(UIUtils.getContext(), HomeDetailActivity.class);
                intent.putExtra("packageName",info.packageName);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public LoadingPage.ResultState onLoad() {
        HomeProtocol protocol=new HomeProtocol();
        mData = protocol.getData(0);
        mPictures = protocol.getPictures();

        return check(mData);
    }


    class HomeAdapter extends MyBaseAdapter<AppInfo> {

        public HomeAdapter(ArrayList<AppInfo> list) {
            super(list);
        }


        @Override
        public ArrayList<AppInfo> onLoadMore() {
            HomeProtocol protocol=new HomeProtocol();
            ArrayList<AppInfo> moreData = protocol.getData(getSize());
            return moreData;
        }


        @Override
        public BaseHolder<AppInfo> getHolder(int position) {
            return new HomeHolder();
        }
    }

}
