package cn.novacomm.igatelibtest.base;

import java.io.Serializable;

import org.json.JSONException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.novacomm.igatelibtest.R;
import cn.novacomm.igatelibtest.framework.Constants;
import cn.novacomm.igatelibtest.util.JsonUtil;
import cn.novacomm.igatelibtest.util.ToastUtil;
import frame.base.FrameFragment;
import frame.commom.AppContext;
import frame.http.HttpCallBack;
import frame.http.bean.HttpRequestBean;
import frame.http.bean.HttpResultBean;
import frame.http.thread.HttpPostJsonThread;

/**
 * @项目名:BaseFragment
 * 
 * @类名:BaseActivity.java
 * 
 * @创建人:pangerwei
 * 
 * @类描述:界面基类
 * 
 * @date:2014-5-27
 * 
 * @Version:1.0
 */
public abstract class BaseFragment extends FrameFragment implements
        OnClickListener {

    // Button act_base_top_leftbtn;
    public HttpRequestBean httpRequestBean;
    // private Handler handler;
    protected Dialog dgLoading, dgNoTitle;
    protected Context CTX = getActivity();

    protected static final boolean ISTEST = BaseActivity.ISTEST;// 测试时为TRUE
                                                                // 正式是为FALSE

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dgLoading = getRequestDg(getActivity());
        dgNoTitle = getRequestDgWithNoTitle(getActivity());
        AppContext.curContext = getActivity();
        
        // handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View settingLayout = inflater.inflate(loadView(), container, false);
        findViews(settingLayout);
        setListener();
        getData();
        return settingLayout;
    }

    /**
     * 初始化布局
     */
    protected abstract void findViews(View view);

    /**
     * 设置监听器
     */
    protected abstract void setListener();

    /**
     * 获取数据
     */
    protected abstract void getData();

    /**
     * 加载本页布局
     * 
     * @return layout的id
     */
    protected abstract int loadView();

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        onKeyEv(v);
    }

    /***
     * 判断EditText是否为空
     */
    public boolean IsEmpty(EditText editText) {
        if (null != editText.getText().toString().trim()
                && !"".equals(editText.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    /***
     * 判断EditText是否为空
     */
    public boolean IsEmpty(TextView editText) {
        if (null != editText.getText().toString().trim()
                && !"".equals(editText.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    /***
     * 回调
     */
    public HttpCallBack callBack = new HttpCallBack() {
        @Override
        public void nullResultHC(int callBackID) {
            super.nullResultHC(callBackID);
            dgLoading.dismiss();
            disNoTitle();
            onFinishWithError("", callBackID);
            alterText("请求出错，可能是您的网速不给力...");
        }

        @Override
        public void successHC(HttpResultBean result, final int callBackID) {
            dgLoading.dismiss();
            disNoTitle();
//            Log.e("result", result.getString());
            onFinishWithError(result.getString(), callBackID);
            try {
                if (Constants.SUCCEED_ONE == JsonUtil.getCode(result
                        .getString())) {
                    onFinish(result.getString(), callBackID);
                } else {
                    alterText(JsonUtil.getMessage(result.getString()));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            super.successHC(result, callBackID);

        }

        @Override
        public void testDataHC(int callBackID) {
            // TODO Auto-generated method stub
            dgLoading.dismiss();
            disNoTitle();
            onFinish("", callBackID);
            super.testDataHC(callBackID);
        }

    };

    /***
     * 网络请求返回的数据 服务器返回成功之后调用
     * 
     * @param content
     */
    public void onFinish(String content, int callBackID) {

    }

    /**
     * 获取字符串 如果是null 或是“null” 则返回“”
     * 
     * @param string
     * @return
     */
    public String getStringWithoutNull(String string) {
        if (null != string && !"".equals(string)) {
            return string;
        }
        return "";
    }

    public boolean isStringWithoutNull(String string) {
        if (null != string && !"".equals(string)) {
            return false;
        }
        return true;
    }

    /***
     * 网络请求返回的数据 带错误
     * 
     * @param content
     */
    public void onFinishWithError(String content, int callBackID) {

    }

    /***
     * 初始化对话框
     * 
     * @param context
     * @return
     */
    public Dialog getRequestDg(Context context) {
        Dialog dgloading;
        dgloading = null;
        dgloading = new Dialog(context, R.style.loadingDialog);
        LinearLayout layout = new LinearLayout(context);

        layout.setBackgroundColor(context.getResources().getColor(
                R.color.transparent));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.bar,
                null);
        layout.addView(view);
        dgloading.setContentView(layout);
        dgloading.setCanceledOnTouchOutside(false);
        return dgloading;
    }

    public Dialog getRequestDgWithNoTitle(Context context) {
        Dialog dgloading;
        dgloading = null;
        dgloading = new Dialog(context, R.style.loadingDialog);
        LinearLayout layout = new LinearLayout(context);

        layout.setBackgroundColor(context.getResources().getColor(
                R.color.transparent));
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.bar_no_title, null);
        layout.addView(view);
        dgloading.setContentView(layout);
        dgloading.setCanceledOnTouchOutside(false);
        return dgloading;
    }

    /**
     * 开启网络请求
     */
    public void StartHttp(HttpRequestBean httpRequestBean,
            HttpCallBack callBack, int callBackID) {
        if (checkConnection(AppContext.curContext)) {
            show();
            HttpPostJsonThread thread = new HttpPostJsonThread(httpRequestBean,
                    callBack, callBackID, ISTEST);// 测试的时候 TRUE 正式传FALSE
            thread.start();
        } else {
            alterText("请求出错，可能是您的网速不给力...");
        }
    }

    /**
     * 开启网络请求
     */
    public void StartHttpWithNoDgTitle(HttpRequestBean httpRequestBean,
            HttpCallBack callBack, int callBackID) {
        if (checkConnection(AppContext.curContext)) {
            showNoTitle();
            HttpPostJsonThread thread = new HttpPostJsonThread(httpRequestBean,
                    callBack, callBackID, ISTEST);// 测试的时候 TRUE 正式传FALSE
            thread.start();
        } else {
            alterText("请求出错，可能是您的网速不给力...");
        }
    }

    /**
     * 开启网络请求 不显示对话框
     * 
     * @param httpRequestBean
     * @param callBack
     * @param callBackID
     */
    public void StartHttpWithNoDialog(HttpRequestBean httpRequestBean,
            HttpCallBack callBack, int callBackID) {
        if (checkConnection(AppContext.curContext)) {
            HttpPostJsonThread thread = new HttpPostJsonThread(httpRequestBean,
                    callBack, callBackID);
            thread.start();
        } else {
            alterText("请求出错，可能是您的网速不给力...");
        }
    }

    /***
     * 显示加载对话框
     */
    public void show() {
        dgLoading.show();
    }

    public void showNoTitle() {
        dgNoTitle.show();
    }

    public void disNoTitle() {
        dgNoTitle.dismiss();
    }

    /**
     * 对话框消失
     */
    public void disMiss() {
        dgLoading.dismiss();
    }

    /***
     * 弹出提示
     */
    public void alterText(String content) {
        ToastUtil.show(content);
    }

    /***
     * 弹出提示
     */
    public void alterText(int id) {
        ToastUtil.show(id);
    }

    /**
     * 点击事件
     * 
     * @param v
     */
    public abstract void onKeyEv(View v);

    /**
     * 获取textview上面内容
     * 
     * @param view
     * @return
     */
    public String getText(TextView view) {
        return null != view ? view.getText().toString().trim() : "";
    }

    /**
     * 获取edittext上的内容
     * 
     * @param view
     * @return
     */
    public String getText(EditText view) {
        return null != view ? view.getText().toString().trim() : "";
    }

    /**
     * 检测网络连接
     * 
     * @return
     */
    public static boolean checkConnection(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * Wifi是否可用
     * 
     * @param mContext
     * @return
     */
    public boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI")) {
            return true;
        }
        return false;
    }

    /**
     * 封装Intent跳转
     * 
     * @param clazz
     *            要跳向的界面的class
     * @param isCloseSelf
     *            是否关闭本界面
     */
    public void goByIntent(Class clazz, boolean isCloseSelf) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
        if (isCloseSelf) {
            getActivity().finish();
        }
    }

    /**
     * 封装Intent跳转
     * 
     * @param key
     * @param obj
     * @param clazz
     * @param isCloseSelf
     */
    protected void goByIntent(String key, Serializable obj, Class<?> clazz,
            boolean isCloseSelf) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), clazz);
        if (!TextUtils.isEmpty(key)) {
            intent.putExtra(key, obj);
        }
        startActivity(intent);
        if (isCloseSelf) {
            getActivity().finish();
        }
    }

    /**
     * 判断sd卡是否存在
     * 
     * @return
     */
    protected boolean isSDCardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 播放视频
     * 
     * @param url
     */
    public void startPlayVideo(String url) {
        Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
        mediaIntent.setDataAndType(Uri.parse(url), "video/mp4");
        getActivity().startActivity(mediaIntent);
    }

}
