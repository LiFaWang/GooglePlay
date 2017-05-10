package tony.com.googleplay.ui.fragment;

import android.view.View;

import tony.com.googleplay.ui.widget.LoadingPage;

/**
 * 游戏
 * Created by Administrator on 2017/4/3.
 */

public class GameFragment extends BaseFragment {
    @Override
    public View onCreateSuccessView() {
        return null;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        return LoadingPage.ResultState.STATE_ERROR;
    }
}
