package tony.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tony.com.googleplay.domain.AppInfo;

/**
 * Created by Administrator on 2017/5/3.
 */

public class HomeDetailProtocol extends BaseProtocol<AppInfo> {
    private String packageName;
    public HomeDetailProtocol(String packageName) {

        this.packageName=packageName;
    }

    @Override
    public String getKey() {
        return "detail";
    }

    @Override
    public String getParams() {
        return "&packageName="+packageName;
    }


    @Override
    public AppInfo parseJson(String result) {
        try {
            JSONObject jo=new JSONObject(result);
            AppInfo info = new AppInfo();
            info.des = jo.getString("des");
            info.downloadUrl = jo.getString("downloadUrl");
            info.iconUrl = jo.getString("iconUrl");
            info.id = jo.getString("id");
            info.name = jo.getString("name");
            info.packageName = jo.getString("packageName");
            info.size = jo.getLong("size");
            info.stars = (float) jo.getDouble("stars");
            info.author = jo.getString("author");
            info.date = jo.getString("date");
            info.downloadNum = jo.getString("downloadNum");
            info.version = jo.getString("version");
            //初始化应用截图信息
            JSONArray ja=jo.getJSONArray("screen");
            ArrayList<String> screen=new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                String str = ja.getString(i);
                screen.add(str);
            }
            info.screen=screen;
            //初始化应用安全信息
            JSONArray ja1=jo.getJSONArray("safe");
            ArrayList<AppInfo.SafeInfo> safe=new ArrayList<>();
            for (int j = 0; j < ja1.length(); j++) {
                JSONObject jo1 = ja1.getJSONObject(j);
                AppInfo.SafeInfo safeInfo=new AppInfo.SafeInfo();
                safeInfo.safeDes = jo1.getString("safeDes");
               safeInfo.safeDesUrl= jo1.getString("safeDesUrl");
                safeInfo.safeUrl=jo1.getString("safeUrl");
                safe.add(safeInfo);
            }
            info.safe=safe;
            return info;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
