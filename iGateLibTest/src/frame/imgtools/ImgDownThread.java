package frame.imgtools;

import java.util.Vector;

import android.os.Handler;
import android.util.Log;
import frame.base.FrameThread;

public class ImgDownThread extends FrameThread{

	private Handler handler;
	private Vector<ImgBean> imglist;
	
	public ImgDownThread(Handler handler,Vector<ImgBean> imglist){
		this.handler=handler;
		this.imglist=imglist;
	}
	
	@Override
	public void run() {

		if(imglist!=null&&imglist.size()>0){
			for (int i = 0; i < imglist.size(); i++) {
				ImgBean img=imglist.get(i);
				try {
					if(isStop)return;
					if(img.getImageDownUrl()!=null&&img.getImageDownUrl().length>0){
						String[] imageUrl=img.getImageDownUrl();
						for (int j = 0; j < imageUrl.length; j++) {
							if(imageUrl[j]!=null){
								ImgUtil.downImg(imageUrl[j]);
							}
						}
						handler.sendEmptyMessage(ImgUtil.IMG_DOWNOK);
					}
				} catch (Exception e) {
					Log.e("出错","下载图片出错"+e.getMessage());
				}
			}
		}
	}
}
