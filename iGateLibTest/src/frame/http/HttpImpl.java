package frame.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONObject;

import frame.http.bean.HttpResultBean;
import frame.util.Log;

public class HttpImpl {
    /**
     * 连接超时时间
     */
    private int connectionTimeout = 10000;
    /**
     * 读取数据超时时间
     */
    private int soTimeout = 10000;

    /**
     * HTTP的GET请求 列如：
     * http.doGet("http://42.121.121.142:8080/novel/newbook.php?page=1");
     * 
     * @param uri
     *            地址
     * @return
     */
    public HttpResultBean doGet(String uri) {
        GetMethod method = new GetMethod(uri);
        try {

            HttpClient client = new HttpClient();
            client.getParams().setConnectionManagerTimeout(connectionTimeout);
            client.getParams().setSoTimeout(soTimeout);

            int code = client.executeMethod(method);
            if (code == 200) {
                String value = method.getResponseBodyAsString();
                Log.e("value", "value:" + value);
                return new HttpResultBean(value);
                // return new JSONObject(value);
            }

            Log.e("请求失败", "请求失败:" + code);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("请求失败", "请求失败:" + e.getMessage());
        }

        return new HttpResultBean(null);
    }

    /**
     * HTTP的Post请求 列如： <br>
     * JSONObject json=new JSONObject(); <br>
     * try { <br>
     * json.put("name", "灰灰11"); <br>
     * json.put("pass", "123456"); <br>
     * catch (JSONException e) { <br>
     * e.printStackTrace(); <br>
     * <br>
     * String
     * signature=ImgUtil.md5("tSzaVWlQFS_1r7XG1v94NitwDXSHDL360qTHorHQZ-E"
     * +json.toString()); <br>
     * http.doPostJSON("http://www.quyeba.com/api/v2/mobile/user_login.json"+
     * "?signature="+signature, json);
     * 
     * @param uri
     *            请求地址
     * @param json
     *            请求参数json对象
     * @return
     */
    public HttpResultBean doPostJSON(String uri, JSONObject json) {
        return doPostString(uri, json.toString());
    }

    /**
     * HTTP的Post请求 列如：<br>
     * JSONObject json=new JSONObject(); <br>
     * try { <br>
     * json.put("name", "灰灰11"); <br>
     * json.put("pass", "123456"); <br>
     * } catch (JSONException e) { <br>
     * e.printStackTrace(); <br>
     * }<br>
     * String
     * signature=ImgUtil.md5("tSzaVWlQFS_1r7XG1v94NitwDXSHDL360qTHorHQZ-E"
     * +json.toString());<br>
     * http.doPostJSON("http://www.quyeba.com/api/v2/mobile/user_login.json"+
     * "?signature="+signature, json,"application/json");<br>
     * 
     * @param uri
     *            请求地址
     * @param json
     *            请求参数json对象
     * @param contentType
     *            http请求头类型 例如 application/json
     * @return
     */
    public HttpResultBean doPostJSON(String uri, JSONObject json,
            String contentType) {
        return doPostString(uri, json.toString(), contentType);
    }

    /**
     * HTTP的Post请求 列如： <br>
     * http.doPostJSON("http://www.quyeba.com/api/v2/mobile/user_login.json",
     * "{\"pass\":\"123456\",\"name\":\"灰灰11\"}");
     * 
     * @param uri
     *            请求地址
     * @param body
     *            请求参数string 类型
     * @return
     */
    public HttpResultBean doPostString(String uri, String body) {
        return doPostString(uri, body, "text/plan");
    }

    /**
     * HTTP的Post请求 列如： <br>
     * http.doPostJSON("http://www.quyeba.com/api/v2/mobile/user_login.json",
     * "{\"pass\":\"123456\",\"name\":\"灰灰11\"}","application/json");
     * 
     * @param uri
     *            请求地址
     * @param body
     *            请求参数string 类型
     * @param contentType
     *            http请求头类型 例如 application/json
     * @return
     */
    public HttpResultBean doPostString(String uri, String body,
            String contentType) {
        try {
            return doPostByte(uri, body.getBytes("utf-8"), contentType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new HttpResultBean(null);
    }

    /**
     * HTTP的Post请求 上传字节
     * 
     * @param uri
     *            请求地址
     * @param body
     *            请求参数
     * @return
     */
    public HttpResultBean doPostByte(String uri, byte[] body) {
        return doPostByte(uri, body, "application/json");
    }

    /**
     * HTTP的Post请求 上传字节
     * 
     * @param uri
     *            请求地址
     * @param body
     *            请求参数
     * @param contentType
     *            http请求头类型 例如 application/json
     * @return
     */
    public HttpResultBean doPostByte(String uri, byte[] body, String contentType) {

        PostMethod method = new PostMethod(uri);
        try {
            RequestEntity requestEntity = new ByteArrayRequestEntity(body,
                    contentType);
            method.setRequestEntity(requestEntity);

            HttpClient client = new HttpClient();
            client.getParams().setConnectionManagerTimeout(connectionTimeout);
            client.getParams().setSoTimeout(soTimeout);

            int code = client.executeMethod(method);
            if (code == 200) {
                String value = method.getResponseBodyAsString();
                Log.e("请求获取数据:", "请求获取数据:" + value);
                return new HttpResultBean(value);
                // return new JSONObject(value);
            }

            Log.e("请求失败", "请求失败");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HttpResultBean(null);
    }

    /**
     * HTTP的Post请求 表单方式提交
     * 
     * @param uri
     *            请求地址
     * @param map
     *            表单键值对 例如 map.put("nickname", "zhangsan5");
     * @return
     */
    public HttpResultBean doPostMap(String uri, Map<String, String> map) {
        return doPostMapWithFile(uri, map);
    }

    /**
     * HTTP的Post请求 表单方式提交 可以附加文件
     * 
     * @param uri
     *            请求地址
     * @param map
     *            表单键值对 例如 map.put("filename", new File(""));
     * @return
     */
    public HttpResultBean doPostMapWithFile(String uri, Map map) {
        return doPostMapWithFile(uri, map, "application/octet-stream");
    }

    /**
     * HTTP的Post请求 表单方式提交 可以附加文件
     * 
     * @param uri
     *            请求地址
     * @param map
     *            表单键值对 例如 map.put("filename", new File(""));
     * @param contentType
     *            http请求头类型 例如 application/json application/octet-stream
     * @return
     */
    public HttpResultBean doPostMapWithFile(String uri, Map map,
            String contentType) {
        return doMethodMapWithFile("post", uri, map, contentType);
    }

    /**
     * HTTP的Put请求 表单方式提交
     * 
     * @param uri
     *            请求地址
     * @param map
     *            表单键值对 例如 map.put("nickname", "zhangsan5");
     * @return
     */
    public HttpResultBean doPutMap(String uri, Map<String, String> map) {
        return doPutMapWithFile(uri, map);
    }

    /**
     * HTTP的Put请求 表单方式提交 可以附加文件
     * 
     * @param uri
     *            请求地址
     * @param map
     *            表单键值对 例如 map.put("filename", new File(""));
     * @return
     */
    public HttpResultBean doPutMapWithFile(String uri, Map map) {
        return doPutMapWithFile(uri, map, "application/octet-stream");
    }

    /**
     * HTTP的Put请求 表单方式提交 可以附加文件
     * 
     * @param uri
     *            请求地址
     * @param map
     *            表单键值对 例如 map.put("filename", new File(""));
     * @param contentType
     *            http请求头类型 例如 application/json application/octet-stream
     * @return
     */
    public HttpResultBean doPutMapWithFile(String uri, Map map,
            String contentType) {
        return doMethodMapWithFile("put", uri, map, contentType);
    }

    private HttpResultBean doMethodMapWithFile(String mflag, String uri,
            Map map, String contentType) {

        try {
            EntityEnclosingMethod method = null;
            if ("put".equals(mflag)) {
                method = new PutMethod(uri);
            } else {
                method = new PostMethod(uri);
            }

            ArrayList<Part> partList = new ArrayList<Part>();

            for (Object key : map.keySet()) {
                if (key == null)
                    continue;
                String str = (String) key;
                Object obj = map.get(str);

                if (obj == null)
                    continue;
                if (obj.getClass() == File.class) {
                    MyFilePart filePart = new MyFilePart(str, (File) obj,
                            contentType);
                    partList.add(filePart);
                } else if (obj.getClass() == File[].class) {
                    File[] farr = (File[]) obj;
                    if (farr != null && farr.length > 0) {
                        for (int i = 0; i < farr.length; i++) {
                            File f = farr[i];
                            if (f != null) {
                                MyFilePart filePart = new MyFilePart(str,
                                        (File) obj, contentType);
                                partList.add(filePart);
                            }
                        }
                    }
                }

                else {
                    UTF8StringPart stringPart = new UTF8StringPart(str,
                            obj.toString());
                    partList.add(stringPart);
                }
            }

            Part[] parts = new Part[partList.size()];
            if (partList.size() > 0) {
                for (int i = 0; i < partList.size(); i++) {
                    parts[i] = partList.get(i);
                }
            }

            method.setRequestEntity(new MultipartRequestEntity(parts, method
                    .getParams()));

            HttpClient client = new HttpClient();
            client.getParams().setConnectionManagerTimeout(connectionTimeout);
            client.getParams().setSoTimeout(soTimeout);

            int code = client.executeMethod(method);
            if (code == 200) {
                String value = method.getResponseBodyAsString();
                Log.e("value", "value:" + value);
                return new HttpResultBean(value);
            }
            Log.e("请求失败", "请求失败");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("请求出错", "请求出错");
        }
        return new HttpResultBean(null);
    }

    class UTF8StringPart extends StringPart {

        public UTF8StringPart(String name, String value) {
            super(name, value);
            setCharSet("UTF-8");
        }

    }

    class MyFilePart extends FilePart {

        String contentType;

        public MyFilePart(String name, File file, String contentType)
                throws FileNotFoundException {
            super(name, file);
            this.contentType = contentType;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

    }

}
