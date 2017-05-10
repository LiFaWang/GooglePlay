package tony.com.googleplay.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import tony.com.googleplay.R;
import tony.com.googleplay.manager.ThreadManager;
import tony.com.googleplay.utils.UIUtils;

/**
 * 根据当前的状态加载不同布局
 * Created by Administrator on 2017/4/3.
 */

public abstract class LoadingPage extends FrameLayout {

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;
    private static final int STATE_UNLOAD = 0;// 未加载
    private static final int STATE_LOADING = 1;// 正在加载
    private static final int STATE_LOAD_EMPTY = 2;// 数据为空
    private static final int STATE_LOAD_ERROR = 3;// 加载失败
    private static final int STATE_LOAD_SUCCESS = 4;// 访问成功
    private int mCurrentState = STATE_UNLOAD;// 当前状态

    public LoadingPage(Context context) {
        super(context);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        if (mLoadingView == null) {//只添加一次
            mLoadingView = onCreateLoadingView();
            addView(mLoadingView);
        }
        if (mEmptyView == null) {
            mEmptyView = onCreateEmptyView();
            addView(mEmptyView);
        }
        if (mErrorView == null) {
            mErrorView = onCreateErrorView();
            addView(mErrorView);
        }
        showRightPage();
        loadData();

    }

    /**
     * 根据当前页面显示正确布局
     */
    private void showRightPage() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility((mCurrentState == STATE_LOADING || mCurrentState == STATE_UNLOAD) ? View.VISIBLE : View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE : View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE : View.GONE);
        }
        if (mSuccessView == null && mCurrentState == STATE_LOAD_SUCCESS) {
            mSuccessView = onCreateSuccessView();
            if (mSuccessView != null) {
                addView(mSuccessView);
            }
        }
        if (mSuccessView != null) {

            mSuccessView.setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 创建加载布局
     *
     * @return
     */
    private View onCreateLoadingView() {
        return UIUtils.inflat(R.layout.layout_loading);
    }

    /**
     * 空布局
     *
     * @return
     */
    private View onCreateEmptyView() {
        return UIUtils.inflat(R.layout.layout_empty);
    }

    /**
     * 创建失败布局
     *
     * @return
     */
    private View onCreateErrorView() {
        View view = UIUtils.inflat(R.layout.layout_error);
        Button btnRetry = (Button) view.findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        return view;
    }

    /**
     * 加载成功布局
     *
     * @return
     */
    public abstract View onCreateSuccessView();

    /**
     * 加载网络数据
     */
    public void loadData() {
        //状态归零
        if (mCurrentState == STATE_LOAD_ERROR || mCurrentState == STATE_LOAD_EMPTY || mCurrentState == STATE_LOAD_SUCCESS || mCurrentState == STATE_LOADING) {
            mCurrentState = STATE_UNLOAD;
        }
        if (mCurrentState == STATE_UNLOAD) {
            ThreadManager.getThreadPoor().excute(new Runnable() {
                @Override
                public void run() {
                    //加载网络数据
                    final ResultState result = onLoad();
                    //必须在主线程更新界面
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result != null) {
                                //更新当前状态
                                mCurrentState = result.getState();
                                //更新当前页面
                                showRightPage();
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 由子类加载网络数据,返回枚举类型的网络家请求状态
     *
     * @return
     */
    public abstract ResultState onLoad();

    /**
     * 使用枚举表示访问网络的几种状态
     */
    public enum ResultState {
        STATE_SUCCESS(STATE_LOAD_SUCCESS), STATE_EMPTY(STATE_LOAD_EMPTY), STATE_ERROR(STATE_LOAD_ERROR);
        private int state;

        ResultState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
