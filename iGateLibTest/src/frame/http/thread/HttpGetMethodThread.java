package frame.http.thread;

import frame.http.HttpImpl;
import frame.http.IHttpCallBack;
import frame.http.bean.HttpRequestBean;
import frame.http.bean.HttpResultBean;

public class HttpGetMethodThread extends HttpThread {

	public HttpGetMethodThread(){
		
	}
	
	public HttpGetMethodThread(HttpRequestBean httpRequestBean, IHttpCallBack callBack, int callBackID) {
		super(httpRequestBean, callBack, callBackID);
	}

	
	public HttpGetMethodThread(HttpRequestBean httpRequestBean, IHttpCallBack callBack, int callBackID, boolean isTest) {
		super(httpRequestBean, callBack, callBackID, isTest);
	}

	
	@Override
	public HttpResultBean doHttpConnection(HttpRequestBean requestBean) {
		HttpImpl httpImpl=new HttpImpl();
		return httpImpl.doGet(requestBean.getUrl());
	}

}
