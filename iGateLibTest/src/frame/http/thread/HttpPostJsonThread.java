package frame.http.thread;

import frame.http.HttpCallBack;
import frame.http.HttpImpl;
import frame.http.HttpImplVideo;
import frame.http.bean.HttpRequestBean;
import frame.http.bean.HttpResultBean;

public class HttpPostJsonThread extends HttpThread {

    public HttpPostJsonThread() {

    }

    public HttpPostJsonThread(HttpRequestBean httpRequestBean,
            HttpCallBack callBack, int callBackID, boolean isTest) {
        super(httpRequestBean, callBack, callBackID, isTest);
    }

    public HttpPostJsonThread(HttpRequestBean httpRequestBean,
            HttpCallBack callBack, int callBackID) {
        super(httpRequestBean, callBack, callBackID);
    }

    @Override
    public HttpResultBean doHttpConnection(HttpRequestBean requestBean) {
        HttpImpl httpImpl = new HttpImpl();

        // return httpImpl.doPostString(requestBean.getUrl(),
        // requestBean.getParam());
        return HttpImplVideo.doPost(requestBean.getUrl(),
                requestBean.getParam());
    }

}
