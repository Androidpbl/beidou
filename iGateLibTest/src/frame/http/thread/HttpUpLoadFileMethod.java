package frame.http.thread;

import frame.http.HttpCallBack;
import frame.http.HttpImpl;
import frame.http.bean.HttpRequestBean;
import frame.http.bean.HttpResultBean;

public class HttpUpLoadFileMethod extends HttpThread {

    public HttpUpLoadFileMethod() {

    }

    public HttpUpLoadFileMethod(HttpRequestBean httpRequestBean,
            HttpCallBack callBack, int callBackID, boolean isTest) {
        super(httpRequestBean, callBack, callBackID, isTest);
    }

    public HttpUpLoadFileMethod(HttpRequestBean httpRequestBean,
            HttpCallBack callBack, int callBackID) {
        super(httpRequestBean, callBack, callBackID);
    }

    @Override
    public HttpResultBean doHttpConnection(HttpRequestBean requestBean) {
        HttpImpl httpImpl = new HttpImpl();

        return httpImpl.doPostMapWithFile(requestBean.getUrl(),
                requestBean.getFileMap());
    }

}
