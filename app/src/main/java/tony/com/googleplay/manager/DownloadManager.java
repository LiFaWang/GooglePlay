package tony.com.googleplay.manager;

import java.util.ArrayList;

/**
 * 下载管理器
 * Created by Administrator on 2017/5/10.
 */

public class DownloadManager {
//    private DownloadManager(){
//
//    }
    //观察者集合
    private ArrayList<DownloadObserver> mObservers=new ArrayList<>();
    private static DownloadManager sInstance=new DownloadManager();
    public static DownloadManager getInstance(){
        return sInstance;
    }

    /**
     * 注册观察者
     * @param observer
     */
    public void registerObserver(DownloadObserver observer){
        if (observer!=null&&!mObservers.contains(observer)){
            mObservers.add(observer);
        }

    }

    /**
     * 3.移除观察者
     * @param observer
     */
    public void removeObserver(DownloadObserver observer){
        if (observer!=null&&mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    /**
     * 4.下载状态发生变化的通知
     */
    public void notifyDownloadStateChanged(){
        for (DownloadObserver observer  :mObservers ) {
            observer.onDownloadStateChanged();
        	
        }
    }

    /**
     * 5.下载进度发生变化的通知
     */
    public void notifyDownloadProgressChanged(){
        for (DownloadObserver observer  :mObservers ) {
            observer.onDownloadProgressChanged();

        }
    }


    /**
     * 1.定义一个观察者接口
     */
    public interface DownloadObserver{
        //下载状态发生变化
        public void onDownloadStateChanged();
        //下载进度发生变化
        public void onDownloadProgressChanged();

    }

}
