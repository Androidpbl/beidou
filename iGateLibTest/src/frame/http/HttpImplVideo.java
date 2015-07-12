package frame.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import frame.http.bean.HttpResultBean;
import frame.util.Log;

public class HttpImplVideo {

    public static HttpResultBean doPost(String url, String conent) {
        HttpPost post4 = new HttpPost(url); //
        try {
            HttpClient httpclient = new DefaultHttpClient();
            // HttpResponse response4 = httpclient.execute(post4);
            // HttpEntity entity4 = response4.getEntity();
            httpclient.getParams().setIntParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            List<NameValuePair> parameters4 = new ArrayList<NameValuePair>();

            post4.addHeader("Authorization", EncryptUtil.encrypt(conent, "md5"));
            parameters4.add(new BasicNameValuePair("data", conent));
            HttpEntity entparams4 = new UrlEncodedFormEntity(parameters4,
                    "UTF-8");
            post4.setEntity(entparams4);

            HttpResponse response4 = httpclient.execute(post4);
            HttpEntity entity4 = response4.getEntity();
            int code = response4.getStatusLine().getStatusCode();
            if (code == 200) {
                String value = EntityUtils.toString(entity4);
                Log.e("请求获取数据:", "请求获取数据:" + value);
                return new HttpResultBean(value);
                // return new JSONObject(value);
            }

            Log.e("请求失败", "请求失败");
            // post4.abort();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new HttpResultBean(null);
    }
}
