package frame.http.thread;

import android.os.Handler;
import frame.base.FrameThread;
import frame.http.IHttpCallBack;
import frame.http.bean.HttpRequestBean;
import frame.http.bean.HttpResultBean;
import frame.util.Log;

public abstract class HttpThread extends FrameThread {

    private HttpRequestBean httpRequestBean;
    private IHttpCallBack callBack;
    private boolean isTest;

    private int callBackID;

    private static final int NULL_CODE = 0x333;
    private static final int SUCCESS_CODE = 0x334;
    private static final int TEST_DATA = 0x335;

    public HttpThread() {

    }

    /**
     * 网络请求
     * 
     * @param httpRequestBean
     *            请求参数
     * @param callBack
     *            回调函数
     * @param callBackID
     *            回调函数ID
     */
    public HttpThread(HttpRequestBean httpRequestBean, IHttpCallBack callBack,
            int callBackID) {
        init(httpRequestBean, callBack, callBackID, false);
    }

    /**
     * 网络请求
     * 
     * @param httpRequestBean
     *            请求参数
     * @param callBack
     *            回调函数
     * @param callBackID
     *            回调函数ID
     * @param isTest
     *            是否启用测试回调
     */
    public HttpThread(HttpRequestBean httpRequestBean, IHttpCallBack callBack,
            int callBackID, boolean isTest) {
        init(httpRequestBean, callBack, callBackID, isTest);
    }

    public void init(HttpRequestBean httpRequestBean, IHttpCallBack callBack,
            int callBackID, boolean isTest) {
        this.httpRequestBean = httpRequestBean;
        this.callBack = callBack;
        this.callBackID = callBackID;
        this.isTest = isTest;
    }

    @Override
    public void run() {
        if (isTest) {
            if (callBack != null) {
                handler.sendEmptyMessage(TEST_DATA);
            }
            return;
        }

        HttpResultBean jobj = doHttpConnection(httpRequestBean);

        if (jobj == null || jobj.getString() == null) {
            if (again(2))
                return;
            // 没有数据
            if (callBack != null) {
                if (isStop)
                    return;
                callBack.nullResultInThreadHC(callBackID);
                handler.sendEmptyMessage(NULL_CODE);
            }
            return;
        }

        if (callBack != null) {
            if (isStop)
                return;
            callBack.successInThreadHC(jobj, callBackID);
            handler.obtainMessage(SUCCESS_CODE, jobj).sendToTarget();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case NULL_CODE:
                if (isStop)
                    return;
                callBack.nullResultHC(callBackID);
                break;
            case SUCCESS_CODE:
                if (isStop)
                    return;
                callBack.successHC((HttpResultBean) msg.obj, callBackID);
                break;
            case TEST_DATA:
                if (isStop)
                    return;
                callBack.testDataHC(callBackID);
                break;
            }

        };
    };

    public static HttpThread getHttpThread(Class threadClass) {
        if (threadClass == null)
            return null;
        Object obj = null;
        try {
            obj = threadClass.newInstance();
        } catch (Exception e) {
            Log.e("threadClass出错", "threadClass没有无参构造方法");
        }
        if (obj == null)
            return null;

        if (obj instanceof HttpThread) {
            HttpThread t = (HttpThread) obj;
            return t;
        }
        Log.e("threadClass传参数出错", "threadClass传参数出错 不属于 HttpThread");
        return null;
    }

    public abstract HttpResultBean doHttpConnection(HttpRequestBean requestBean);
}
