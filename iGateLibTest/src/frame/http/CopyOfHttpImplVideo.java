//package frame.http;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.httpclient.Header;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import frame.http.bean.HttpResultBean;
//import frame.util.Log;
//
//public class CopyOfHttpImplVideo {
//
//    /**
//     * 连接超时时间
//     */
//    private static int connectionTimeout = 10000;
//    /**
//     * 读取数据超时时间
//     */
//    private static int soTimeout = 10000;
//    public static HttpResultBean doPost(String url, String conent) {
//        PostMethod method = new PostMethod(url);
////        HttpPost post4 = new HttpPost(url);
//        try {
//            HttpClient httpClient = new HttpClient();
//            List<NameValuePair> parameters4 = new ArrayList<NameValuePair>();
//            method.addRequestHeader(new Header("Authorization", EncryptUtil.encrypt(conent, "md5")));
////            post4.addHeader("Authorization", EncryptUtil.encrypt(conent, "md5"));
//            parameters4.add(new BasicNameValuePair("data", conent));
//            HttpEntity entparams4 = new UrlEncodedFormEntity(parameters4,
//                    "UTF-8");
////            post4.setEntity(entparams4);
//            method.set
//            int code = httpClient.executeMethod(method);
//            HttpEntity entity4 = response4.getEntity();
////            int code = response4.getStatusLine().getStatusCode();
//            if (code == 200) {
//                String value = EntityUtils.toString(entity4);
//                Log.e("请求获取数据:", "请求获取数据:" + value);
//                return new HttpResultBean(value);
//                // return new JSONObject(value);
//            }
//
//            Log.e("请求失败", "请求失败");
//            // post4.abort();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return new HttpResultBean(null);
//    }
//}
