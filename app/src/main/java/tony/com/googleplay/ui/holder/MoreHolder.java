package tony.com.googleplay.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import tony.com.googleplay.R;
import tony.com.googleplay.utils.UIUtils;

/**
 * 加载更多的holder
 * Created by Administrator on 2017/4/10.
 */

public class MoreHolder extends BaseHolder<Integer> {
    /*
    正在加载更多
    没有更多加载
    加载更多失败
     */
    public static final int TYPE_HAS_MORE=0;
    public static final int TYPE_NO_MORE=1;
    public static final int TYPE_MORE_ERROR=2;

//    @BindView(R.id.ll_loadMore)
    LinearLayout mLlLoadMore;
//    @BindView(R.id.tv_error)
    TextView mTvError;
//根据是否可以加载更多，设置当前数据，一旦设置数据马上refresh
    public MoreHolder(boolean hasMore) {
        setData(hasMore?TYPE_HAS_MORE:TYPE_NO_MORE);
    }

    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.list_item_more);
//        ButterKnife.bind(view);
        mLlLoadMore= (LinearLayout) view.findViewById(R.id.ll_loadMore);
        mTvError= (TextView)view.findViewById(R.id.tv_error);
        return view;
    }

    @Override
    public void refreshView(Integer data) {
        switch (data) {
            case  TYPE_HAS_MORE ://加载更多
                mLlLoadMore.setVisibility(View.VISIBLE);
                mTvError.setVisibility(View.GONE);
                break;
            case  TYPE_NO_MORE ://不能加载更多
                mLlLoadMore.setVisibility(View.GONE);
                mTvError.setVisibility(View.GONE);
                break;
            case  TYPE_MORE_ERROR ://加载更多失败
                mLlLoadMore.setVisibility(View.GONE);
                mTvError.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }

    }
}
