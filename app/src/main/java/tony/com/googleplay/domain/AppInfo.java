package tony.com.googleplay.domain;

import java.util.ArrayList;

/**
 * 首页应用信息
 * Created by Administrator on 2017/4/11.
 */

public class AppInfo {
        public String des;
        public String downloadUrl;
        public String iconUrl;
        public String id;
        public String name;
        public String packageName;
        public long size;
        public float stars;
        // 应用详情页额外字段:
        public String author;
        public String date;
        public String downloadNum;
        public String version;

        public ArrayList<String> screen;

        @Override
        public String toString() {
                return "AppInfo{" +
                        "des='" + des + '\'' +
                        ", downloadUrl='" + downloadUrl + '\'' +
                        ", iconUrl='" + iconUrl + '\'' +
                        ", id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", packageName='" + packageName + '\'' +
                        ", size=" + size +
                        ", stars=" + stars +
                        ", author='" + author + '\'' +
                        ", date='" + date + '\'' +
                        ", downloadNum='" + downloadNum + '\'' +
                        ", version='" + version + '\'' +
                        ", screen=" + screen +
                        ", safe=" + safe +
                        '}';
        }

        public ArrayList<SafeInfo> safe;

        public static class SafeInfo {
                public String safeDes;
                public String safeDesUrl;
                public String safeUrl;

                @Override
                public String toString() {
                        return "SafeInfo{" +
                                "safeDes='" + safeDes + '\'' +
                                ", safeDesUrl='" + safeDesUrl + '\'' +
                                ", safeUrl='" + safeUrl + '\'' +
                                '}';
                }
        }
}
