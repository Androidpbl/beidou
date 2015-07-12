package frame.http.bean;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import frame.http.IHttpCallBack;
import frame.http.IThreadManage;
import frame.http.thread.HttpThread;

public class HttpRequestBean {

    /**
     * 启动 线程类
     */
    private Class threadClass;

    /**
     * 地址
     */
    private String url;
    /**
     * json
     */
    private String param;
    /**
     * 表单提交json时json的key
     */
    private String key = "data";
    /**
     * 文件上传 map
     */
    private Map fileMap;
    /**
     * 文件上次类型
     */
    private String contentType;

    public HttpRequestBean() {

    }

    public HttpRequestBean(String url) {
        this.url = url;
    }

    public HttpRequestBean(String url, String param) {
        this.url = url;
        this.param = param;
    }

    public HttpRequestBean(String url, JSONObject jsonObject) {
        this.url = url;
        if (jsonObject != null) {
            JSONObject object = new JSONObject();
            try {
                object.put("data", jsonObject.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.param = object.toString();
            Log.i("info", "param ---->   " + param);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        // JSONObject object = new JSONObject();
        // try {
        // object.put("data", param);
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        this.param = param;
        Log.e("info", "setParam ---->   " + this.param);
    }

    public Map<String, String> getMapParam() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, param);
        return map;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFileMap(Map fileMap) {
        this.fileMap = fileMap;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map getFileMap() {
        return fileMap;
    }

    public Class getThreadClass() {
        return threadClass;
    }

    public void setThreadClass(Class threadClass) {
        this.threadClass = threadClass;
    }

    public void connect(IHttpCallBack callBack, int callBackID,
            IThreadManage iThreadManage) {
        connect(callBack, callBackID, false, iThreadManage, null);
    }

    public void connect(IHttpCallBack callBack, int callBackID,
            IThreadManage iThreadManage, String threadKey) {
        connect(callBack, callBackID, false, iThreadManage, threadKey);
    }

    public void connect(IHttpCallBack callBack, int callBackID, boolean isTest,
            IThreadManage iThreadManage, String threadKey) {
        HttpThread thread = HttpThread.getHttpThread(threadClass);
        if (thread != null) {
            thread.init(this, callBack, callBackID, isTest);
            thread.start(iThreadManage, threadKey);
        }
    }

}
