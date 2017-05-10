package tony.com.googleplay.ui.holder;

import android.view.View;

import tony.com.googleplay.R;
import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.utils.UIUtils;


/**
 *
 * Created by Administrator on 2017/5/9.
 */

public class DetailDownLoadHolder extends BaseHolder<AppInfo> {
    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.layout_detail_download);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {

    }
}
