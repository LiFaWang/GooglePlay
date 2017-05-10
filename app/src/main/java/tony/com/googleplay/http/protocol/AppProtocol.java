package tony.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tony.com.googleplay.domain.AppInfo;

/**
 * 首页网络数据
 * Created by Administrator on 2017/4/11.
 */

public class AppProtocol extends BaseProtocol<ArrayList<AppInfo>> {
    @Override
    public String getKey() {
        return "app";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<AppInfo>  parseJson(String result) {
        // 解析json方式:遇到{}就用JsonObject, 遇到[]就用JsonArray
        // Gson.jar
        try {
            JSONArray ja=new JSONArray(result);
            // 应用列表解析
            ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                AppInfo info = new AppInfo();
                info.des = jo.getString("des");
                info.downloadUrl = jo.getString("downloadUrl");
                info.iconUrl = jo.getString("iconUrl");
                info.id = jo.getString("id");
                info.name = jo.getString("name");
                info.packageName = jo.getString("packageName");
                info.size = jo.getLong("size");
                info.stars = (float) jo.getDouble("stars");
                appList.add(info);
            }
            return appList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}
