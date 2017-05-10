package tony.com.googleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *线程池管理器
 * Created by Administrator on 2017/5/9.
 */

public class ThreadManager {
    private static ThreadPoor mThreadPoor;

    /**
     * 获取单例的线程池对象
     * @return
     */
    public static ThreadPoor getThreadPoor(){
        if(mThreadPoor==null){
            synchronized (ThreadManager.class){
                if (mThreadPoor == null) {
                    int cpuNum = Runtime.getRuntime().availableProcessors();
//                    int count=cpuNum*2+1;
                    int count=8;
                    mThreadPoor = new ThreadPoor(count,count,0L);

                }
            }
        }
        return mThreadPoor;

    }
    public static class ThreadPoor{
        public ThreadPoor(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private ThreadPoolExecutor executor;

        public void excute(Runnable r){
            if(executor==null){
                executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,
                        TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy());
            }
             executor.execute(r);
        }

    }
}
