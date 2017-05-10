package tony.com.googleplay.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import com.lzy.okgo.OkGo;

/**
 * 在清单文件中声明使用当前类
 * Created by Administrator on 2017/4/2.
 */

public class BaseApplication extends Application {

    private static Context mContext;
    private static Handler mHandler;
    private static int mMainThreadId;

    public static Context getmContext() {
        return mContext;
    }

    public static Handler getmHandler() {
        return mHandler;
    }

    public static int getmMainThreadId() {
        return mMainThreadId;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //1.获取Context对象
        mContext = getApplicationContext();
        //2.创建handler
        mHandler = new Handler();
        //3.获取当前的线程
        mMainThreadId = Process.myTid();
        //必须调用初始化
        OkGo.init(this);

    }
}
