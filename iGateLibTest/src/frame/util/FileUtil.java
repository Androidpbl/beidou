package frame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;

public class FileUtil {

	public static String getRealPath(Context mContext, Uri fileUrl) {
		String fileName = null;
		Uri filePathUri = fileUrl;
		
		File f = new File( Environment.getExternalStorageDirectory() +"");
		
		if (fileUrl != null) {
			if (fileUrl.getScheme().toString().compareTo("content") == 0) // content://开头的uri
			{
				Cursor cursor = mContext.getContentResolver().query(fileUrl,
						null, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaColumns.DATA);
					fileName = cursor.getString(column_index); // 取出文件路径
					
					
					if (f.getAbsolutePath().startsWith("/mnt")&&!fileName.startsWith("/mnt")) {
						// 检查是否有”/mnt“前缀

						fileName = "/mnt" + fileName;
					}
					cursor.close();
				}
			} else if (fileUrl.getScheme().compareTo("file") == 0) // file:///开头的uri
			{
				fileName = filePathUri.toString();
				fileName = filePathUri.toString().replace("file://", "");
				// 替换file://
				if (f.getAbsolutePath().startsWith("/mnt")&&!fileName.startsWith("/mnt")) {
					// 加上"/mnt"头
					fileName = "/mnt" + fileName;
				}
			}
		}
		return fileName;
	}
	
	
	
	public static void writeSerializable(Object obj,String path) {
		ObjectOutputStream out =null;
		try {
			File f = new File(path);
			f.getParentFile().mkdirs();
			if (f.exists())
				f.delete();
			f.createNewFile();
			out= new ObjectOutputStream( new FileOutputStream(f));
			out.writeObject(obj);
		} catch (Exception e) {
			Log.e("序列化出错", "write序列化出错："+e.getMessage());
			e.printStackTrace();
		}finally{
			if(out !=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Object readSerializable(String path) {
		ObjectInputStream in = null;
		try {
			File f = new File(path);
			if (f.exists()) {
				in = new ObjectInputStream(new FileInputStream(f));
				Object mul = in.readObject();
				return mul;
			}
		} catch (Exception e) {
			Log.e("序列化出错", "read序列化出错："+e.getMessage());
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/***
	 * 保存序列化集合
	 * 
	 * @param aid
	 *            文件名
	 * @param list
	 *            列表参数
	 */
	public static void write(Context mContext, String fileName,
	        ArrayList<Object> list) {
	    
	    try {
//	        File f = new File(mContext.getFilesDir() + File.separator
//	                + fileName);
	        File f = new File(Environment.getExternalStorageDirectory()+"/"+fileName+"/"
	                + fileName);
	        
	        ObjectOutputStream out = new ObjectOutputStream(
	                new FileOutputStream(f));
	        ((ObjectOutput) out).writeObject(list);
	        out.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * 读序列化集合
	 * 
	 * @param fileName
	 *            文件名
	 * @return list 集合
	 */
	public static ArrayList<Object> read(Context mContext,String fileName) {
	    ObjectInputStream in = null;
	    try {
//	        File f = new File(mContext.getFilesDir() + File.separator
//	                + fileName);
	        File f = new File(Environment.getExternalStorageDirectory() + "/"+fileName+"/"+fileName);
	        if (f.exists()) {
	            in = new ObjectInputStream(new FileInputStream(f));
	            ArrayList<Object> list = (ArrayList<Object>) in
	                    .readObject();
	            return list;
	        }
	    } catch (Exception e) {
	    } finally {
	        if (in != null) {
	            try {
	                in.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return null;
	}
	
	public static int getLength(Context mContext,String fileName){
	    ObjectInputStream in = null;
	    int length = 0;
        try {
//            File f = new File(mContext.getFilesDir() + File.separator
//                    + fileName);
            File f = new File(Environment.getExternalStorageDirectory() +"/"+fileName+"/"+ fileName);
            if (f.exists()) {
                in = new ObjectInputStream(new FileInputStream(f));
                length = in.available();
            }
        } catch (Exception e) {
            
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return length;
	}
	
}
