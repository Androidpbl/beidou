package frame.base;

import android.support.v4.app.Fragment;
import frame.http.IHttpCallBack;
import frame.http.IThreadManage;
import frame.http.ThreadManager;
import frame.http.bean.HttpResultBean;

public class FrameFragment extends Fragment  implements IHttpCallBack,IThreadManage{

	public FrameFragment(){
		
	}
	
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
	public void onDestroy() {
		ThreadManager.stopAllRun(this);
		super.onDestroy();
	}


	@Override
	public void nullResultHC(int callBackID) {
		
	}

	@Override
	public void successHC(HttpResultBean result, int callBackID) {
		
	}

	@Override
	public void nullResultInThreadHC(int callBackID) {
		
	}

	@Override
	public void successInThreadHC(HttpResultBean result, int callBackID) {
		
	}

	@Override
	public void testDataHC(int callBackID) {
		
	}
	
}
