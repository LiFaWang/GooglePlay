package tony.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import tony.com.googleplay.domain.SubjectInfo;

/**
 * Created by Administrator on 2017/4/12.
 */

public class SubProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {
    @Override
    public String getKey() {
        return "subject";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<SubjectInfo> parseJson(String result) {
        try {
            JSONArray ja=new JSONArray(result);
            //列表
            ArrayList<SubjectInfo> list=new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                SubjectInfo info=new SubjectInfo();
                info.des=jo.getString("des");
                info.url=jo.getString("url");
                list.add(info);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
