package tony.com.googleplay.ui.fragment;

import android.view.View;

import java.util.ArrayList;

import tony.com.googleplay.domain.CategoryInfo;
import tony.com.googleplay.http.protocol.CategoryProtocol;
import tony.com.googleplay.ui.adapter.MyBaseAdapter;
import tony.com.googleplay.ui.holder.BaseHolder;
import tony.com.googleplay.ui.holder.CategoryHolder;
import tony.com.googleplay.ui.holder.TitleHolder;
import tony.com.googleplay.ui.widget.LoadingPage;
import tony.com.googleplay.ui.widget.MyListView;
import tony.com.googleplay.utils.UIUtils;

/**
 * 分类
 * Created by Administrator on 2017/4/3.
 */

public class CategoryFragment extends BaseFragment {

    private ArrayList<CategoryInfo> mData;

    @Override
    public View onCreateSuccessView() {
        MyListView view=new MyListView(UIUtils.getContext());
        view.setAdapter(new CategoryAdapter(mData));
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        CategoryProtocol protocol=new CategoryProtocol();
        mData = protocol.getData(0);
        return check(mData);
    }
    class CategoryAdapter extends MyBaseAdapter<CategoryInfo>{

        public CategoryAdapter(ArrayList<CategoryInfo> list) {
            super(list);
        }

        @Override
        public boolean hasMore() {
            return false;
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        @Override
        public int getInnerType(int position) {
            CategoryInfo info = getItem(position);
            if(info.isTitle){
                return super.getInnerType(position)+1;
            }else {
                return super.getInnerType(position);

            }
        }

        @Override
        public BaseHolder<CategoryInfo> getHolder(int position) {
            CategoryInfo info = getItem(position);
            if (info.isTitle){
                return new TitleHolder();
            }else {
                return new CategoryHolder();
            }

        }
        @Override
        public ArrayList<CategoryInfo> onLoadMore() {
            return null;
        }

    }
}
