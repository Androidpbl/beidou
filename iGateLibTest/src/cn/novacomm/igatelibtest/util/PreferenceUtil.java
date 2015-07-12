package cn.novacomm.igatelibtest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;



/**    
 * @项目名:DownwindCar  
 * 
 * @类名:PreferenceUtil.java  
 * 
 * @创建人:pangerwei 
 *
 * @类描述:配置文件类
 * 
 * @date:2014-1-13
 * 
 * @Version:1.0 
 */ 
public class PreferenceUtil {

    private PreferenceUtil(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
        }
    }

    public static PreferenceUtil preferenceUtil;

    private static SharedPreferences sharedPreferences;
    private static Editor editor;

    /**
     * 获取实例对象
     * 
     * @param context
     * @return
     */
    public static PreferenceUtil getInstance(Context context) {
        if (preferenceUtil == null) {
            preferenceUtil = new PreferenceUtil(context);
        }
        return preferenceUtil;
    }

    /**
     * 保存string值
     * 
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    /**
     * 保存boolean值
     * 
     * @param key
     * @param value
     */
    public void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value).commit();
    }

    /**
     * 保存boolean值
     * 
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    /**
     * 获取string值
     * 
     * @param key
     * @return
     */
    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    /**
     * 获取int值
     * 
     * @param key
     * @return
     */
    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * 获取boolean值
     * 
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

  
}
