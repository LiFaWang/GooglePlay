package tony.com.googleplay.ui.holder;

import android.view.View;
import android.widget.TextView;

import tony.com.googleplay.R;
import tony.com.googleplay.domain.CategoryInfo;
import tony.com.googleplay.utils.UIUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class TitleHolder extends BaseHolder<CategoryInfo> {

    private TextView mTextView;

    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.list_item_title);
        mTextView = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        mTextView.setText(data.title);
    }
}
