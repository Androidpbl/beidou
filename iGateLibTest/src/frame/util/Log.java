package frame.util;

/**********************
 * 
 * 
 * @类名:Log.java
 * 
 * @创建人:cuijianzhi
 * 
 * @类描述:日志工具类
 * 
 * @date:2013-9-23
 * 
 * @Version:1.0
 */

public class Log {

    public static boolean isLog = true;

    public static void e(String tag, String msg) {
        if (isLog)
            android.util.Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isLog)
            android.util.Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isLog)
            android.util.Log.e(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isLog)
            android.util.Log.e(tag, msg);
    }

    public static void syso(String str) {
        if (isLog)
            System.out.println(str);
    }

}
