package tony.com.googleplay.ui.holder;

import android.view.View;

/**
 * Created by Administrator on 2017/4/9.
 */

public abstract class BaseHolder<T>{
    private final View mRootView;
    private T data;
    public BaseHolder() {
        //初始化布局
        mRootView = initView();
        //设置tag
        mRootView.setTag(this);
    }
    public View getRootView() {
        return mRootView;
    }

    /**
     * 得到数据
     * @param data
     */
    public void setData(T data) {
        this.data = data;
        if(data!=null){
            refreshView(data);//刷新数据

        }
    }

    public T getData() {
        return data;
    }
    /**
     * 初始化布局的方法必须由子类实现
     * @return
     */
    public abstract View initView() ;
    public abstract void refreshView(T data);

}
