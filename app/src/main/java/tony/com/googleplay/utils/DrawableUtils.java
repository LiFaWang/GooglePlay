package tony.com.googleplay.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by Administrator on 2017/4/13.
 */

public class DrawableUtils {
    public static GradientDrawable getGradientDrawable(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setCornerRadius(radius);
        drawable.setColor(color);
        return drawable;
    }
    /**
     * 生成状态选择器
     *
     * @param press
     * @param normal
     * @return
     */
    public static StateListDrawable getSelector(Drawable press, Drawable normal) {
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed}, press);
        selector.addState(new int[]{}, normal);
        return selector;
    }

    public static StateListDrawable getSelector(int normalColor,
                                                int pressColor, int radius) {
        GradientDrawable normal = getGradientDrawable(normalColor, radius);
        GradientDrawable press = getGradientDrawable(pressColor, radius);
        return getSelector(press, normal);
    }
}
