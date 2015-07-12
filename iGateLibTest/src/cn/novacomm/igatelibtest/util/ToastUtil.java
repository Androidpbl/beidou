package cn.novacomm.igatelibtest.util;

import android.widget.Toast;
import frame.commom.AppContext;

/**
 * @项目名:DownwindCar
 * 
 * @类名:ToastUtil.java
 * 
 * @创建人:pangerwei
 * 
 * @类描述:吐司工具类
 * 
 * @date:2013-12-2
 * 
 * @Version:1.0
 */
public class ToastUtil {

    public static void show(String text) {
        Toast.makeText(AppContext.curContext, text, 0).show();
    }

    public static void show(int id) {
        Toast.makeText(AppContext.curContext,
                AppContext.curContext.getString(id), 0).show();
    }
}
