package cn.novacomm.igatelibtest.framework;

import java.util.Random;

import android.os.Environment;

/**
 * @项目名:VideoPass
 * 
 * @类名:Constants.java
 * 
 * @创建人:pangerwei
 * 
 * @类描述:常量类
 * 
 * @date:2014-5-27
 * 
 * @Version:1.0
 */
public class Constants {

    /** 成功 */
    public static final int SUCCEED = 0;
    /** 成功 */
    public static final int SUCCEED_FOUR = 4;
    /** 成功 */
    public static final int SUCCEED_THREE = 3;
    /** 成功 */
    public static final int SUCCEED_TWO = 2;
    /** 成功 */
    public static final int SUCCEED_ONE = 1;
    /** 失败 */
    public static final int FALSE = -1;
    /** 失败 */
    public static final int FALSE_ONE = -2;

    /**
     * 新地址
     */
    public static final String URL = "http://203.86.54.99:9008";
    // public static final String URL = "http://203.86.54.83:9008";

    /**
     * 首页当前事件删除
     */
    public static final int HOME_CURRENT_EVENT_DELETE = 2221;
    /**
     * 首页一般事件删除
     */
    public static final int HOME_GENERAL_EVENT_DELETE = 2222;

    /**
     * 首页删除
     */
    public static final int HOME_DELETE = 2223;

    /**
     * 事件上移
     */
    public static final int HOME_ITEM_UP = 2224;

    /**
     * 事件下移
     */
    public static final int HOME_ITEM_DOWN = 2225;

    /**
     * 现场图片轮训时间间隔
     */
    public static final int CURRENT_PIC_ROATION_TIME = 5 * 1000;

    /**
     * 首页数据轮训时间间隔
     */
    public static final int HOME_PAGE_ROATION_TIME = 30 * 1000;

    /**
     * 聊天轮训时间间隔
     */
    public static final int MESSAGE_ROATION_TIME = 2 * 1000;

    /**
     * 语音文件保存路径
     */
    public static final String BASE_AUDIO_SEND_PATH = Environment
            .getExternalStorageDirectory() + "/videopass/voiceDown/";

    /************** 图片保存路径 **/
    public static String IMGCACHE = Environment.getExternalStorageDirectory()
            + "/videopass/imgCache";
    /************** carsh 保存路径 **/
    public static final String CRASH_SAVEPATH = Environment
            .getExternalStorageDirectory() + "/videopass/crash/";

    /**
     * 是否首次进入软件
     */
    public static String ISFIRST = "isfirst";

    /**
     * 用户权限
     */
    public static final String PERMISSION = "permission";
    /**
     * 用户单位
     */
    public static final String UNIT = "unit";
    /**
     * 管理员权限
     */
    public static final String MANAGER_PER = "0";
    /**
     * 现场权限
     */
    public static final String CURRENT_PER = "-1";
    /**
     * 查看权限
     */
    public static final String CHECK_PER = "-2";

    /**
     * 图片下载地址
     */
    // public static final String IAMGE_DOWN_URL =
    // "http://192.168.0.33:9083/video";
    public static final String IAMGE_DOWN_URL = URL + "/video";
    // public static final String IAMGE_DOWN_URL =
    // "http://203.86.54.83:9008/video";

    /**
     * 服务器图片上传地址
     */
    // public static final String HTTP_IMAGE_UPLOAD_URL =
    // "http://203.86.54.83:9008/video/service/uploadpicture.htm";
    // public static final String HTTP_IMAGE_UPLOAD_VOICE_URL =
    // "http://203.86.54.83:9008/video/service/uploadimage.htm";
    public static final String HTTP_IMAGE_UPLOAD_VOICE_URL = URL
            + "/video/service/uploadimage.htm";
    public static final String HTTP_IMAGE_UPLOAD_URL = URL
            + "/video/service/uploadpicture.htm";

    /**
     * token
     */
    public static final String TOKEN = "sfc9588";

    /**
     * 获取时间戳
     * 
     * @return
     */
    public static String getTimeStamp() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 获取随机数
     * 
     * @return
     */
    public static String getRandom() {
        return new Random().nextInt(999) + "";
    }

    /**
     * 获取请求地址
     * 
     * @return
     */
    public static String getRequestURL() {
        // String timeStamp = getTimeStamp();
        // String random = getRandom();
        // String signature = ImgUtil.md5(TOKEN + timeStamp + random);
        // return "http://192.168.0.33:9083/video/service/httpservices.htm";
        return URL + "/video/service/httpservices.htm";
        // return "http://192.168.10.8:8080/video/service/httpservices.htm";
    }
}
