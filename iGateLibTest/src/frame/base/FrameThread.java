package frame.base;

import frame.http.IThreadManage;


public class FrameThread extends Thread{

	public boolean isStop;
	public int times;
	
	public FrameThread(){
		
	}
	
	public void stopRun(){
		isStop=true;
	}
	
	
	public boolean again(int t){
		if(isStop)return false;
		if(times<t){
			times++;
			run();
			return true;
		}
		return false;
	}
 
	

	public void start(IThreadManage manager) {
		manager.runThread(this,null);
	}
	
	public void start(IThreadManage manager,String flag) {
		manager.runThread(this,flag);
	}
	
 
}
