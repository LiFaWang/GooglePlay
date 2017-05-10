package tony.com.googleplay.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/4/12.
 */

public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
        initView();

    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView(){
        setDivider(null);//去掉分割线
        setCacheColorHint(Color.TRANSPARENT);//将缓冲色换成透明色
        setSelector(new ColorDrawable());//系统默认状态选择器为透明色（去掉默认点击效果）
    }
}
