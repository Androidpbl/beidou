package frame.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Environment;

public class Cache<T> {

	/**
	 * 文件对象缓存路径
	 */
	public static String  CACHEDATA = Environment.getExternalStorageDirectory() + "/fileCache/cachedata1/";
	
	public static Map<String,Object> CacheMap=new ConcurrentHashMap<String,Object>();
	// 创建一个固定大小的线程池
	private static  ExecutorService service = Executors.newFixedThreadPool(1);
	
	public static void put(String key,Object obj){
		service.execute(new MyRunnable(key, obj));
	}
	
	public static Object getObj(String key){
		Object obj=CacheMap.get(key);
		if(obj==null){
			obj=FileUtil.readSerializable(CACHEDATA+""+MD5.md5(key));
		}
		return obj;
	}
	
	public  T get(String key){
		Object obj=CacheMap.get(key);
		if(obj==null){
			Log.e("取缓存", "从序列化文件中取");
			obj=FileUtil.readSerializable(CACHEDATA+""+MD5.md5(key));
		}
		return (T)obj;
	}

}



class MyRunnable implements Runnable {

	public  String key;
	public  Object obj;
	
	public MyRunnable(String key, Object obj) {
		this.key = key;
		this.obj = obj;
	}

	@Override
	public void run() {
		Cache.CacheMap.put(key, obj);
		FileUtil.writeSerializable(obj, Cache.CACHEDATA+""+MD5.md5(key));
	}
	
}
