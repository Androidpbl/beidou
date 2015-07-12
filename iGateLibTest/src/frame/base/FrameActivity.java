package frame.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import frame.commom.AppContext;
import frame.http.IHttpCallBack;
import frame.http.IThreadManage;
import frame.http.ThreadManager;
import frame.http.bean.HttpResultBean;

public class FrameActivity extends FragmentActivity  implements IHttpCallBack,IThreadManage {

	public void runThread(FrameThread baseThread){
		ThreadManager.runThread(this, baseThread, null);
    }
	    
	public void runThread(FrameThread baseThread,String threadKey){
		ThreadManager.runThread(this, baseThread, threadKey);
	}
	
 
	public void stopRunThread(String threadKey) {
		ThreadManager.stopRun(this, null, threadKey);
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppContext.curContext = this;
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		AppContext.curContext = this;
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ThreadManager.stopAllRun(this);
	}

	/**
	 * HttpCallBack 空数据的时候
	 */
	@Override
	public void nullResultHC(int callBackID) {
		
	}

	/**
	 * HttpCallBack 成功的时候
	 */
	@Override
	public void successHC(HttpResultBean result, int callBackID) {
		
	}

	/** 
	 * HttpCallBack 空数据的时候 （线程中）
	 */
	@Override
	public void nullResultInThreadHC(int callBackID) {
		
	}

	/**
	 * HttpCallBack 成功的时候 (线程中)
	 */
	@Override
	public void successInThreadHC(HttpResultBean result, int callBackID) {
		
	}

	/**
	 * HttpCallBack 测试数据
	 */
	@Override
	public void testDataHC(int callBackID) {
		
	}

 
 
}
