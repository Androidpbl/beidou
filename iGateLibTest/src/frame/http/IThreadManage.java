package frame.http;

import frame.base.FrameThread;

public interface IThreadManage {

	public void runThread(FrameThread baseThread,String flag);
	
	public void stopRunThread(String flag);
	
}
