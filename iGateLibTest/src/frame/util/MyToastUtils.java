package frame.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 
 * @项目名:LuXunMuseum
 * 
 * @类名:MyToastUtils.java
 * 
 * @创建人:cuijianzhi
 * 
 * @类描述:Toast工具类
 * 
 * @date:2013-9-23
 * 
 * @Version:1.0
 */
public class MyToastUtils {

    /**
     * 普通的toast
     * 
     * @param context
     * @param content
     */
    public static void getNormalToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();

    }

    /**
     * 居中的普通的toast
     * 
     * @param context
     * @param content
     */
    public static void getNormalCenterToast(Context context, String content) {

        Toast toast = new Toast(context);
        toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 中心稍微偏下的toast
     * 
     * @param context
     * @param content
     */
    public static void getcendownToast(Context context, String content) {

        Toast toast = new Toast(context);
        toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 150);
        toast.show();
    }

}
