package tony.com.googleplay.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import tony.com.googleplay.R;

/**
 * Created by Administrator on 2017/4/13.
 */

public class RatioLayout extends FrameLayout {

    private float mRatio;

    public RatioLayout(@NonNull Context context) {
        super(context);
    }

    public RatioLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义控件下属性集合
        TypedArray typedArray = context.getResources().obtainAttributes(attrs, R.styleable.RatioLayout);
        // 根据属性名称,获取对应属性值 : 系统自动生成的id = 自定义控件名_属性名
        mRatio = typedArray.getFloat(R.styleable.RatioLayout_ratio, 0);
        typedArray.recycle();// 回收类型集合, 节省内存
    }

    public RatioLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1. 获取控件的宽度
        // 2. 根据比例,计算出高度
        // 3. 用最新的宽度和高度来测量控件

        // MeasureSpec.AT_MOST;至多, 类似wrap_content
        // MeasureSpec.EXACTLY;确定,类似写死成dp或者match_parent
        // MeasureSpec.UNSPECIFIED;未确定
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        // ratio要大于0, 并且宽度确定,高度不确定, 才有必要计算高度值
        if (mRatio>0&&widthMode==MeasureSpec.EXACTLY&&heightMode!=MeasureSpec.EXACTLY){
            // 1. 获取控件的宽度
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //获取图片的宽度
            int imageWidth=width-getPaddingLeft()-getPaddingRight();
            int imageHeight= (int) (imageWidth/mRatio+0.5f);
            int height=imageHeight+getPaddingTop()+getPaddingBottom();
            //重新定义高度模式
            MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);

        }
        //使用最新的高度来测量布局
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
