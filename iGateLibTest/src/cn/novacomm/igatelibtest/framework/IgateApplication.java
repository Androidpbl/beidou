package cn.novacomm.igatelibtest.framework;

import android.app.Application;
import android.content.Context;
import frame.imgtools.ImgUtil;

public class IgateApplication extends Application {
    private static IgateApplication app = null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        // 捕获异常
        // CrashHandler crashHandler = CrashHandler.getInstance();
        // crashHandler.init(getApplicationContext(), this);
        app = this;

    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        ImgUtil.recycle();
        super.onLowMemory();
    }

    public static IgateApplication getApplication(Context context) {
        return (IgateApplication) context.getApplicationContext();
    }


    public static IgateApplication getInstance() {
        return app;
    }

   
}
