package frame.http;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import frame.base.FrameThread;

public class ThreadManager {

	private static Map<IThreadManage,Vector<FrameThread>> RunThreadListMap=new ConcurrentHashMap<IThreadManage, Vector<FrameThread>>();
	private static Map<IThreadManage,Map<String,FrameThread>> RunThreadMap=new ConcurrentHashMap<IThreadManage, Map<String,FrameThread>>();
	
	public static void add(IThreadManage iThreadManage,FrameThread frameThread,String threadKey){
		if(RunThreadListMap.containsKey(iThreadManage)){
			Vector<FrameThread> vector=RunThreadListMap.get(iThreadManage);
			vector.add(frameThread);
		}else{
			Vector<FrameThread> vector=new Vector<FrameThread>();
			vector.add(frameThread);
			RunThreadListMap.put(iThreadManage, vector);
		}
		
		if(threadKey!=null){
			Map<String,FrameThread> map=RunThreadMap.get(iThreadManage);
			if(map==null){
				ConcurrentHashMap<String,FrameThread> cmap=new ConcurrentHashMap<String,FrameThread>();
				cmap.put(threadKey, frameThread);
				RunThreadMap.put(iThreadManage, cmap);
			}else{
				map.put(threadKey, frameThread);
			}
		}
	}
	
	public static FrameThread get(IThreadManage iThreadManage,String threadKey){
		Map<String,FrameThread> map=RunThreadMap.get(iThreadManage);
		if(map==null||threadKey==null)return null;
		return map.get(threadKey);
	}
	
	private static FrameThread remove(IThreadManage iThreadManage,String threadKey){
		Map<String,FrameThread> map=RunThreadMap.get(iThreadManage);
		if(map==null||threadKey==null)return null;
		return map.remove(threadKey);
	}
	
	public static void stopRun(IThreadManage iThreadManage,FrameThread frameThread){
		stopRun(iThreadManage, frameThread, null);
	}
	public static void stopRun(IThreadManage iThreadManage,String threadKey){
		stopRun(iThreadManage, null, threadKey);
	}
	
	public static void stopRun(IThreadManage iThreadManage,FrameThread frameThread,String threadKey){
		if (frameThread==null&&threadKey==null){
			return;
		}
		Map<String,FrameThread> map=RunThreadMap.get(iThreadManage);
		if(threadKey!=null){
			frameThread=remove(iThreadManage, threadKey);
		}
		if(frameThread!=null){
			frameThread.stopRun();
			map.remove(threadKey);
		}
	}
	
	public static void stopAllRun(IThreadManage iThreadManage){
		Map<String,FrameThread> map=RunThreadMap.remove(iThreadManage);
		Vector<FrameThread> list=RunThreadListMap.remove(iThreadManage);
		if(list!=null){
			for (FrameThread frameThread : list) {
				frameThread.stopRun();
			}
			list.clear();
		}
	}
	
	
	public static void runThread(IThreadManage iThreadManage,FrameThread baseThread,String threadKey){
		if(baseThread==null)return;
		if(threadKey==null){
			runThread(iThreadManage,baseThread);
			return;
		}
		ThreadManager.stopRun(iThreadManage, threadKey);
		baseThread.start();
		ThreadManager.add(iThreadManage, baseThread,threadKey);
	}

	private static void runThread(IThreadManage iThreadManage,FrameThread baseThread) {
		if(baseThread==null)return;
    	baseThread.start();
    	ThreadManager.add(iThreadManage, baseThread,null);
	}
	
}
