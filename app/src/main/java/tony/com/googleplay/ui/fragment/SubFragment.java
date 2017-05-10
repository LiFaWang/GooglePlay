package tony.com.googleplay.ui.fragment;

import android.view.View;

import java.util.ArrayList;

import tony.com.googleplay.domain.SubjectInfo;
import tony.com.googleplay.http.protocol.SubProtocol;
import tony.com.googleplay.ui.adapter.MyBaseAdapter;
import tony.com.googleplay.ui.holder.BaseHolder;
import tony.com.googleplay.ui.holder.SubHolder;
import tony.com.googleplay.ui.widget.LoadingPage;
import tony.com.googleplay.ui.widget.MyListView;
import tony.com.googleplay.utils.UIUtils;

/**
 * 专题
 * Created by Administrator on 2017/4/3.
 */

public class SubFragment extends BaseFragment {

    private ArrayList<SubjectInfo> mData;

    @Override
    public View onCreateSuccessView() {
        MyListView view=new MyListView(UIUtils.getContext());
        view.setAdapter(new SubAdapter(mData));
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        SubProtocol protocol=new SubProtocol();
        mData = protocol.getData(0);

        return check(mData);
    }
    class SubAdapter extends MyBaseAdapter{

        public SubAdapter(ArrayList list) {
            super(list);
        }

        @Override
        public ArrayList<SubjectInfo> onLoadMore() {
            SubProtocol protocol=new SubProtocol();
            ArrayList<SubjectInfo> moreDate = protocol.getData(getSize());
            return moreDate;
        }

        @Override
        public BaseHolder getHolder(int position) {
            return new SubHolder();
        }
    }
}
