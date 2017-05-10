package tony.com.googleplay.ui.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tony.com.googleplay.R;
import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.utils.UIUtils;

/**
 * Created by Administrator on 2017/5/8.
 */

public class DetailDesInfoHolder extends BaseHolder<AppInfo> {
    @BindView(R.id.tv_detail_des)
    TextView mTvDetailDes;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.rl_detail_toggle)
    RelativeLayout mRlDetailToggle;
    @BindView(R.id.tv_detail_author)
    TextView mTvDetailAuthor;
    private ViewGroup.LayoutParams mParams;
    private boolean isOpen;


    @Override
    public View initView() {
        View view = UIUtils.inflat(R.layout.layout_detail_desinfo);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        if (data != null) {
            mTvDetailDes.setText(data.des);
            mTvDetailAuthor.setText(data.author);
            UIUtils.getHandler().post(new Runnable() {
                @Override
                public void run() {

                    // 默认高度最多7行
                    int shortHeight = getShortHeight();
                    mParams = mTvDetailDes.getLayoutParams();
                    mParams.height = shortHeight;
                    mTvDetailDes.setLayoutParams(mParams);

                }
            });

        }


    }

    /**
     * 展开或收起的动画
     */
    protected void toggle() {
        ValueAnimator animator = null;
        int shortHeight = getShortHeight();
        int longHeight = getLongHeight();


        if (longHeight > shortHeight) {
            // / 只有描述内容大于7行时,才有必要展开或收起
            animator = ValueAnimator.ofInt(shortHeight, longHeight);
            if (!isOpen) {
                animator.start();
            } else {
                animator.reverse();
            }
            isOpen = !isOpen;
        }


        if (animator != null) {
            final ValueAnimator finalAnimator = animator;
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator arg0) {
                    mParams.height = (Integer) arg0.getAnimatedValue();

                    mTvDetailDes.setLayoutParams(mParams);
                    mIvArrow.setRotation(finalAnimator.getAnimatedFraction() * 180);
                }
            });

            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator arg0) {

                }

                @Override
                public void onAnimationRepeat(Animator arg0) {

                }

                @Override
                public void onAnimationEnd(Animator arg0) {
//                    if (isOpen) {
//                        mIvArrow.setImageResource(R.drawable.arrow_up);
//                    } else {
//                        mIvArrow.setImageResource(R.drawable.arrow_down);
//                    }

                    // 让ScrollView滑动到底部
                    final ScrollView scrollView = getScrollView();

                    // 需要注意的是，该方法不能直接被调用
                    // 因为Android很多函数都是基于消息队列来同步，所以需要一部操作，
                    // addView完之后，不等于马上就会显示，而是在队列中等待处理，虽然很快，但是如果立即调用fullScroll，
                    // view可能还没有显示出来，所以会失败
                    // 应该通过handler在新线程中更新
                    UIUtils.getHandler().post(new Runnable() {

                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

                @Override
                public void onAnimationCancel(Animator arg0) {

                }
            });

            animator.setDuration(200);
        }

    }

    /**
     * 当textview展示7行时的高度
     *
     * @return
     */
    private int getShortHeight() {
        int width = mTvDetailDes.getMeasuredWidth();// 原始textView宽度

        // 宽高模式要和原始textView保持一致
        // 宽度大小模式
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width,
                View.MeasureSpec.EXACTLY);
        // 高度大小模式, 高度最大是2000px
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000,
                View.MeasureSpec.AT_MOST);

        // 模拟一个TextView
        TextView textView = new TextView(UIUtils.getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);// 大小和原始textview一致
        textView.setText(getData().des);// 文字
        textView.setMaxLines(7);// 最多展示7行

        textView.measure(widthMeasureSpec, heightMeasureSpec);

        return textView.getMeasuredHeight();
    }

    /**
     * textView展示完整高度
     *
     * @return
     */
    private int getLongHeight() {
        int width = mTvDetailDes.getMeasuredWidth();// 原始textView宽度

        // 宽高模式要和原始textView保持一致
        // 宽度大小模式
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width,
                View.MeasureSpec.EXACTLY);
        // 高度大小模式, 高度最大是2000px
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000,
                View.MeasureSpec.AT_MOST);

        // 模拟一个TextView
        TextView textView = new TextView(UIUtils.getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);// 大小和原始textview一致
        textView.setText(getData().des);// 文字

        textView.measure(widthMeasureSpec, heightMeasureSpec);

        return textView.getMeasuredHeight();
    }

    /**
     * 获取ScrollView对象 从子控件一直向上找,直到找到ScrollView为止 注意: 一定要保证当前布局中有ScrollView
     */
    private ScrollView getScrollView() {
        ViewParent parent = mTvDetailDes.getParent();// 获取父控件
        while (!(parent instanceof ScrollView)) {
            parent = parent.getParent();
        }

        return (ScrollView) parent;
    }

    @OnClick({R.id.rl_detail_toggle,R.id.tv_detail_des})
    public void onViewClicked() {
        toggle();
    }
}
