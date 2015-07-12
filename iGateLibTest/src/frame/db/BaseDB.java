package frame.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class BaseDB {
//	private static final int DB_VERSION = 6; // 数据库版本号
//	private static final String DB_NAME = "qyb.db";// 数据库名
//	private SQLiteOpenHelper dbOpenHelper;
//	private Context xContext;
	public SQLiteDatabase db;
 
	/**
	 * 空间不够存储的时候设为只读
	 * 
	 * @throws SQLiteException
	 */
	public void open() throws SQLiteException
	{
		Log.e("open", "open打开数据库");
//		dbOpenHelper = new DBOpenHelper(xContext, getDBName(), null, getDBVersion());
		try
		{
			db = getSqLiteOpenHelper().getWritableDatabase();
		} catch (SQLiteException e)
		{
			db = getSqLiteOpenHelper().getReadableDatabase();
		}
	}

	public void close()
	{
		if (db != null&db.isOpen())
		{
			db.close();
			db = null;
		}
	}
	
	public abstract SQLiteOpenHelper getSqLiteOpenHelper();
	 
	
	public void closeCursor(Cursor cursor){
		if(cursor!=null&&!cursor.isClosed()){
			cursor.close();
		}
	}
}
