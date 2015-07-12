package frame.http.thread;

import frame.http.HttpCallBack;
import frame.http.HttpImpl;
import frame.http.bean.HttpRequestBean;
import frame.http.bean.HttpResultBean;

public class HttpPostKeyJsonThread extends HttpThread{

	public HttpPostKeyJsonThread(){
		
	}
	
	public HttpPostKeyJsonThread(HttpRequestBean httpRequestBean,
			HttpCallBack callBack, int callBackID, boolean isTest) {
		super(httpRequestBean, callBack, callBackID, isTest);
	}

	public HttpPostKeyJsonThread(HttpRequestBean httpRequestBean,
			HttpCallBack callBack, int callBackID) {
		super(httpRequestBean, callBack, callBackID);
	}

	@Override
	public HttpResultBean doHttpConnection(HttpRequestBean requestBean) {
		HttpImpl httpImpl=new HttpImpl();
		
		return httpImpl.doPostMap(requestBean.getUrl(), requestBean.getMapParam());
	}

}
