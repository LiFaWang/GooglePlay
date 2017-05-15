package tony.com.googleplay.manager;

import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.domain.DownloadInfo;
import tony.com.googleplay.http.HttpHelper;
import tony.com.googleplay.utils.IOUtils;
import tony.com.googleplay.utils.UIUtils;

/**
 * 下载管理器
 * Created by Administrator on 2017/5/10.
 */

public class DownloadManager {
    public static final int STATE_UNDO = 0;// 未下载
    public static final int STATE_WAITING = 1;// 等待下载
    public static final int STATE_DOWNLOADING = 2;// 正在下载
    public static final int STATE_PAUSE = 3;// 下载暂停
    public static final int STATE_ERROR = 4;// 下载失败
    public static final int STATE_SUCCESS = 5;// 下载成功
    private DownloadManager(){

    }
    //观察者集合
    private ArrayList<DownloadObserver> mObservers=new ArrayList<>();
    //下载对象的集合
    private ConcurrentHashMap<String,DownloadInfo>mDownloadInfoHashMap=new ConcurrentHashMap<>();
    //下载任务的集合
    private ConcurrentHashMap<String,DownloadTask>mDownloadTaskHashMap=new ConcurrentHashMap<>();
    private static DownloadManager sInstance=new DownloadManager();


    public static DownloadManager getInstance(){
        return sInstance;
    }

    /**
     * 注册观察者
     * @param observer
     */
    public synchronized void registerObserver(DownloadObserver observer){
        if (observer!=null&&!mObservers.contains(observer)){
            mObservers.add(observer);
        }

    }

    /**
     * 3.移除观察者
     * @param observer
     */
    public synchronized void removeObserver(DownloadObserver observer){
        if (observer!=null&&mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    /**
     * 4.下载状态发生变化的通知
     */
    public  synchronized void notifyDownloadStateChanged(DownloadInfo info){
        for (DownloadObserver observer  :mObservers ) {
            observer.onDownloadStateChanged(info);
        	
        }
    }

    /**
     * 5.下载进度发生变化的通知
     */
    public synchronized void notifyDownloadProgressChanged(DownloadInfo info){
        for (DownloadObserver observer  :mObservers ) {
            observer.onDownloadProgressChanged(info);

        }
    }


    /**
     * 1.定义一个观察者接口
     */
    public interface DownloadObserver{
        //下载状态发生变化
        public void onDownloadStateChanged(DownloadInfo info);
        //下载进度发生变化
        public void onDownloadProgressChanged(DownloadInfo info);

    }
    /**
     *开始下载
     */
    public synchronized void download(AppInfo appInfo){
        //断点续传，首先判断是否之前下载过，如果下载过，用之前的对象继续下载
        DownloadInfo info=mDownloadInfoHashMap.get(appInfo.id);
        if(info==null){
            info = DownloadInfo.copy(appInfo);//创建新的下载对象
        }
        info.currentState=STATE_WAITING;//等待下载
        notifyDownloadStateChanged(info);//通知所有的观察者
        mDownloadInfoHashMap.put(info.id,info);//将Info对象放入集合中
        //开始下载
        DownloadTask task=new DownloadTask(info);
        ThreadManager.getThreadPoor().excute(task);
        //将下载任务保存到集合
        mDownloadTaskHashMap.put(info.id,task);


    }
     class DownloadTask implements Runnable{
       private DownloadInfo info;
        public DownloadTask(DownloadInfo info) {
            this.info=info;

        }

        @Override
        public void run() {
            //访问网络，下载数据
        info.currentState=STATE_DOWNLOADING;
            notifyDownloadStateChanged(info);
            //初始化下载文件对象
            File file=new File(info.path);
         HttpHelper.HttpResult mHttpResult=null;
            //判断文件是否存在，决定是否是第一次下载,如果文件长度不合法也要重新下载
            if (!file.exists()||file.length()!=info.currentPosition||info.currentPosition==0){
                //第一次下载
                file.delete();//删除废弃的文件
                info.currentPosition=0;
                mHttpResult = HttpHelper.download(HttpHelper.URL + "download?name=" + info.downloadUrl);
            }else {
                //继续下载 range参数表示的是从文件的那个位置开始下载
                mHttpResult = HttpHelper.download(HttpHelper.URL + "download?name=" + info.downloadUrl+"&range"+file.length());

            }
            if (mHttpResult!=null && mHttpResult.getInputStream()!=null){

                InputStream in = mHttpResult.getInputStream();
                FileOutputStream out=null ;
                try {
                     out=new FileOutputStream(file,true);//参数2：表示如果文件已经存在，是否要追加在当前文件上
                    int len;
                    byte[]buffer=new byte[1024];
                    //只有状态是下载才读文件，否则立即停止
                    while ((len=in.read(buffer))!=-1&&info.currentState==STATE_DOWNLOADING){
                        out.write(buffer,0,len);
                        out.flush();//将缓存区的文件写出去
                        info.currentPosition+=len;
                        //下载进度更新
                        notifyDownloadProgressChanged(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    IOUtils.close(in);
                    IOUtils.close(out);

                }
                //下载结束
                if(file.length()==info.size){//校验文件完整性
                    info.currentState=STATE_SUCCESS;
                    notifyDownloadStateChanged(info);

                }else if(info.currentState==STATE_PAUSE) {
                    notifyDownloadStateChanged(info);

                 }else  {
                    //下载失败
                    file.delete();
                    info.currentState=STATE_ERROR;
                    info.currentPosition=0;
                    notifyDownloadStateChanged(info);
                }
            }else {
                //下载失败
                file.delete();
                info.currentState=STATE_ERROR;
                info.currentPosition=0;
                notifyDownloadStateChanged(info);
            }
            //下载结束
            //从下载任务的集合移除下载任务
            mDownloadTaskHashMap.remove(info.id);
        }
    }

    /**
     * 暂停下载
     * @param
     */
    public synchronized void pause(AppInfo appInfo){

        DownloadInfo info=mDownloadInfoHashMap.get(appInfo.id);
        if (info!=null){
            if (info.currentState==STATE_WAITING||info.currentState==STATE_DOWNLOADING){
                DownloadTask task = mDownloadTaskHashMap.get(info.id);
                if (task!=null){
                    //停止任务，从线程池中移除当前任务
                    ThreadManager.getThreadPoor().cancle(task);
                }
                //更新下载状态
                info.currentState=STATE_PAUSE;
                //通知当前变化
                notifyDownloadStateChanged(info);
            }

        }

    }

    /**
     * 安装app
     * @param appInfo
     */
    public synchronized  void install(AppInfo appInfo){
        DownloadInfo downloadInfo = mDownloadInfoHashMap.get(appInfo.id);
        // 只有下载成功, 才可以安装
        if (downloadInfo != null && downloadInfo.currentState == STATE_SUCCESS) {
            // 跳到系统的安装页面进行安装
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
                    "application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);
        }
    }

    /**
     * 根据下载信息获取对象
     * @param appInfo
     * @return
     */
    public DownloadInfo getDownloadInfo(AppInfo appInfo){
        if(appInfo!=null){
            return mDownloadInfoHashMap.get(appInfo.id);
        }
        return null;
    }


}
