package tony.com.googleplay.ui.fragment;

import android.view.View;

import java.util.ArrayList;

import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.http.protocol.AppProtocol;
import tony.com.googleplay.ui.adapter.MyBaseAdapter;
import tony.com.googleplay.ui.holder.AppHolder;
import tony.com.googleplay.ui.holder.BaseHolder;
import tony.com.googleplay.ui.widget.LoadingPage;
import tony.com.googleplay.ui.widget.MyListView;
import tony.com.googleplay.utils.UIUtils;

/**
 * 应用
 * Created by Administrator on 2017/4/3.
 */

public class AppFragment extends BaseFragment {

    private ArrayList<AppInfo> date;

    @Override
    public View onCreateSuccessView() {
        MyListView view=new MyListView(UIUtils.getContext());
            view.setAdapter(new AppAdapter(date));
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        AppProtocol protocol=new AppProtocol();
        date = protocol.getData(0);
        return check(date);
    }
    class AppAdapter extends MyBaseAdapter<AppInfo>{

        public AppAdapter(ArrayList<AppInfo> list) {
            super(list);
        }


        @Override
        public BaseHolder<AppInfo> getHolder(int position) {
            return new AppHolder();
        }
        @Override
        public ArrayList<AppInfo> onLoadMore() {
            AppProtocol protocol=new AppProtocol();
            ArrayList<AppInfo> moreDate = protocol.getData(getSize());
            return moreDate;
        }

    }

}
