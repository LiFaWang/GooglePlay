package tony.com.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/13.
 */

public class RecommendProtocol extends BaseProtocol<ArrayList<String>> {

    @Override
    public String getKey() {
        return "recommend";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<String> parseJson(String result) {
        try {
            JSONArray ja=new JSONArray(result);
            ArrayList<String> list=new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                String string = ja.getString(i);
                list.add(string);
            }
           return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
