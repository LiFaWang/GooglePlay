package tony.com.googleplay.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import tony.com.googleplay.global.BaseApplication;

/**
 * Created by Administrator on 2017/4/2.
 */

public class UIUtils {
    public static Context getContext(){
        return BaseApplication.getmContext();
    }
    public static Handler getHandler(){
        return BaseApplication.getmHandler();
    }
    public static int getMainThreadId(){
        return BaseApplication.getmMainThreadId();
    }

    /**
     * 根据id获取字符串
     * @param id
     * @return
     */
    public static String getString(int id){
        return getContext().getResources().getString(id);
    }

    /**
     * 根据id获取图片
     * @param id
     * @return
     */
    public static Drawable getDrawable(int id){
        return getContext().getResources().getDrawable(id);
    }

    /**
     * 加载颜色状态选择器
     * @param tabTextColorResId
     * @return
     */
    public static ColorStateList getColorStateList(int tabTextColorResId) {
        return getContext().getResources().getColorStateList(tabTextColorResId);
    }
    /**
     * 根据id获取颜色
     * @param id
     * @return
     */
    public static int getColor(int id){
        return getContext().getResources().getColor(id);
    }
    /**
     * 根据id获取尺寸
     * @param id
     * @return
     */
    public static int getDimen(int id){
        return getContext().getResources().getDimensionPixelSize(id);
    }
    /**
     * 根据id获取字符数组
     * @param id
     * @return
     */
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }

    /**
     * dp转px
     * @param dp
     * @return
     */
    public static int dip2px(float dp){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density*dp+0.5);

    }

    /**
     * px 转dp
     * @param px
     * @return
     */
    public static float px2dip(int px){
        float density = getContext().getResources().getDisplayMetrics().density;
        return px/density;
    }

    /**
     * 加载布局文件
     * @param layoutId
     * @return
     */
    public static View inflat(int layoutId){
        return View.inflate(getContext(), layoutId, null);
    }

    /**
     * 判断是否运行在主线程
     * @return
     */
    public static boolean isRunOnUIThread(){
        return getMainThreadId()==android.os.Process.myTid();
    }

    /**
     * 保持运行在主线程
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable){
        if(isRunOnUIThread()){
            runnable.run();
        }else {
            getHandler().post(runnable);
        }

    }



}
