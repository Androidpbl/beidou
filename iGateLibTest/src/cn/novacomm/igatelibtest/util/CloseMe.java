package cn.novacomm.igatelibtest.util;

import java.util.LinkedList;

import android.app.Activity;

public class CloseMe {
	private static LinkedList<Activity> acys =new LinkedList<Activity>();
	
	
	public static void add(Activity acy){
		acys.add(acy);
	}
	
	public static void remove(Activity acy){
		acys.remove(acy);
	}
	
	public static void close(){
		Activity acy;
		while(acys.size()!=0){
			acy=acys.poll();
			if(!acy.isFinishing()){
				acy.finish();
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
