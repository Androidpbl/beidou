package frame.http;

import frame.http.bean.HttpResultBean;

public abstract class HttpCallBack implements IHttpCallBack{

	public void nullResultHC(int callBackID){};
	
	public void successHC(HttpResultBean result,int callBackID){};
	
	public void nullResultInThreadHC(int callBackID){};
	
	public void successInThreadHC(HttpResultBean result,int callBackID){};
	
	public void testDataHC(int callBackID){};
	
}
