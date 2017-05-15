package tony.com.googleplay.domain;

import android.os.Environment;

import java.io.File;

import tony.com.googleplay.manager.DownloadManager;

/**
 *
 * Created by Administrator on 2017/5/10.
 */

public class DownloadInfo {
    public String id;
    public String name;
    public String packageName;
    public String downloadUrl;
    public String path;//下载到本地的路径
    public int currentState;//当前下载状态
    public long size;
    public int currentPosition;//当前的下载位置
    public static final String GOOGLE_MARKET = "google_market";//sd卡根目录下的文件夹
    public static final String DOWNLOAD = "download";//google_market目录下的文件夹

    /**
     * 获取当前的状态
     *
     * @return 2
     */
    public float getProgress() {
        if (size > 0) {
            return currentPosition / (float) size;
        }
        return 0;
    }

    /**
     * @return 获取文件路径
     */

    public String getFliePath() {
        StringBuffer sb = new StringBuffer();
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        sb.append(sdcard);
        sb.append(File.separator);//分割线/
        sb.append(GOOGLE_MARKET);
        sb.append(File.separator);//分割线/
        sb.append(DOWNLOAD);
        if (createDir(sb.toString())) {
            return sb.toString() + File.separator + name + ".apk";
        }
        return null;
    }

    /**
     * 创建文件夹
     *
     * @param dir
     * @return
     */
    private boolean createDir(String dir) {
        File dirFile = new File(dir);
        //如果文件夹不存在或者不是文件夹
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return dirFile.mkdirs();//创建文件夹

        }
        return true;
    }

    public static DownloadInfo copy(AppInfo appInfo) {
        DownloadInfo info = new DownloadInfo();
        info.id = appInfo.id;
        info.packageName = appInfo.packageName;
        info.name = appInfo.name;
        info.downloadUrl = appInfo.downloadUrl;
        info.path = info.getFliePath();
        info.size = appInfo.size;
        info.currentPosition = 0;
        info.currentState = DownloadManager.STATE_UNDO;
        return info;
    }


}
