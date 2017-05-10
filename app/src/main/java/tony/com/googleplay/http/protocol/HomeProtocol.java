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

public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {
    public ArrayList<String> getPictures() {
        return mPictures;
    }

    private ArrayList<String> mPictures;// 轮播条的图片集合
    @Override
    public String getKey() {
        return "home";
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
            JSONObject jo = new JSONObject(result);

            // 轮播条数据解析
            mPictures = new ArrayList<String>();
            JSONArray ja = jo.getJSONArray("picture");
            for (int i = 0; i < ja.length(); i++) {
                String str = ja.getString(i);
                mPictures.add(str);
            }

            // 应用列表解析
            ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
            JSONArray ja1 = jo.getJSONArray("list");
            for (int i = 0; i < ja1.length(); i++) {
                JSONObject jo1 = ja1.getJSONObject(i);

                AppInfo info = new AppInfo();
                info.des = jo1.getString("des");
                info.downloadUrl = jo1.getString("downloadUrl");
                info.iconUrl = jo1.getString("iconUrl");
                info.id = jo1.getString("id");
                info.name = jo1.getString("name");
                info.packageName = jo1.getString("packageName");
                info.size = jo1.getLong("size");
                info.stars = (float) jo1.getDouble("stars");

                appList.add(info);
            }

            return appList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}
