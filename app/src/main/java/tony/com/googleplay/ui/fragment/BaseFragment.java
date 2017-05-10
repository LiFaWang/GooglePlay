package tony.com.googleplay.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tony.com.googleplay.ui.widget.LoadingPage;
import tony.com.googleplay.utils.UIUtils;

/**
 * Created by Administrator on 2017/4/3.
 */

public abstract class BaseFragment extends Fragment {
    private LoadingPage mLoadingPage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLoadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View onCreateSuccessView() {
                return BaseFragment.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return BaseFragment.this.onLoad();
            }
        };

        return mLoadingPage;
    }
    //由子类创建布局的方法
    public abstract View onCreateSuccessView();
    //有子类实现加载网络数据的逻辑
    public abstract LoadingPage.ResultState onLoad();
    public void loadData(){
        if (mLoadingPage!=null){
            mLoadingPage.loadData();
        }
    }

    /**
     * 检查返回状态
     * @param obj
     * @return
     */
    public LoadingPage.ResultState check(Object obj){
        if (obj!=null){

            if(obj instanceof ArrayList){
                ArrayList list= (ArrayList) obj;
                if(!list.isEmpty()){
                    return LoadingPage.ResultState.STATE_SUCCESS;
                }else {
                    return LoadingPage.ResultState.STATE_EMPTY;

            }
            }
        }
        return LoadingPage.ResultState.STATE_ERROR;
    }
}
