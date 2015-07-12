package cn.novacomm.igatelibtest.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @项目名:DownwindCar
 * 
 * @类名:JsonUtil.java
 * 
 * @创建人:pangerwei
 * 
 * @类描述:json解析工具类
 * 
 * @date:2013-12-9
 * 
 * @Version:1.0
 */
public class JsonUtil {

    /**
     * 获取返回码
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static int getCode(String json) throws JSONException {
        return new JSONObject(json).optInt("result");
    }

    /**
     * 获取返回信息
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static String getMessage(String json) throws JSONException {
        return new JSONObject(json).optString("info");
    }

    /**
     * 获取返回的URL
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static String getUrl(String json) throws JSONException {
        return new JSONObject(json).optString("url");
    }

  

    /**
     * 获取管理员人数
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static int getManagerCounts(String json) throws JSONException {
        return new JSONObject(json).optInt("member");
    }

    
    /**
     * 获取用户权限
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static String getPermission(String json) throws JSONException {
        return new JSONObject(json).optString("permission");
    }

    /**
     * 获取用户权限
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static String getUnit(String json) throws JSONException {
        return new JSONObject(json).optString("unit");
    }

    
    /**
     * 获取摄像头数目
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static String getVideoCount(String json) throws JSONException {
        JSONObject object = new JSONObject(json);

        return object.optJSONObject("data").optString("cameracount");
    }

}
