package frame.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*************************************************
 * 格式验证工具类 包括:电话号码,座机号码. Email邮箱,邮编,网址 汉字,密码格式, 身份证号,等验证
 * 
 * @author duanxinmeng
 * @version 1.0
 * @date 2013-8-30
 */
public class CheckUtil {

    /**********************************************
     * 检测手机号码是否符合格式
     * 
     * @param number
     *            手机号码
     * @return true 手机号码符合规范 flase 手机号不符合规范
     */
    public static boolean checkPhoneNumber(String number) {
        try {
            Pattern p = Pattern
                    .compile("(13[0-9]|14[57]|15[012356789]|18[02356789])\\d{8}");
            Matcher m = p.matcher(number);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**********************************************
     * 检查座机号码 可输入的格式: "XXX-XXXXXXX"、"XXXX-XXXXXXXX"、
     * "XXX-XXXXXXX"、"XXX-XXXXXXXX"、 "XXXXXXX"和"XXXXXXXX"
     * 
     * @param machineNumber
     *            座机号码
     * @return true 号码格式正确 false 格式有误
     */
    public static boolean checkMachineNumber(String machineNumber) {
        try {
            Pattern p = Pattern
                    .compile("([0-9]{7,8})|[0-9]{4}|[0-9]{3}-[0-9]{7,8}");
            Matcher m = p.matcher(machineNumber);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**********************************************
     * 检测Email是否符合规范
     * 
     * @param str
     *            检测Email
     * @return
     */
    public static boolean checkEmail(String email) {
        // 定义正则字符串
        String strPatten = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPatten);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**********************************************
     * 检测字符串是否为url网址 如:http://www.
     * 
     * @param url
     * @return true URl地址合法 false 不合法的url地址
     */
    public static boolean checkInternetUrl(String url) {
        // 定义正则字符串
        String strPatten = "^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
        Pattern p = Pattern.compile(strPatten);
        Matcher m = p.matcher(url);
        return m.matches();
    }

    /**********************************************
     * 检测字符串是否都是汉字
     * 
     * @param hanzi
     * @return true 符合规范,全是汉字 false 不符合规范
     */
    public static boolean checkHanzi(String hanzi) {
        // 定义正则字符串
        String strPatten = "^[\u4e00-\u9fa5]{0,}$";
        Pattern p = Pattern.compile(strPatten);
        Matcher m = p.matcher(hanzi);
        return m.matches();
    }

    /**********************************************
     * 密码格式验证 必须以字母开头, 密码只能由数字,字母,下划线组成
     * 
     * @param passWord
     * @return true 密码符合规范 false 密码不符合规范
     */
    public static boolean checkPassWord(String passWord) {
        // 定义正则字符串
        String strPatten = "^[a-zA-Z]\\w{6,16}$";
        Pattern p = Pattern.compile(strPatten);
        Matcher m = p.matcher(passWord);
        return m.matches();
    }

    /**********************************************
     * 验证身份证号码
     * 
     * @param id_number
     *            身份证号码
     * @return true 身份证符合规范 false 身份证有误
     */
    public static Boolean checkNID(String number) {
        String pattern = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**********************************************
     * 验证邮编是否合法
     * 
     * @param postCode
     *            邮编
     * @return true 邮编符合规范 false 邮编不符合规范
     */
    public static boolean checkPostCode(String postCode) {
        if (postCode.matches("[1-9]\\d{5}(?!\\d)")) {
            return true;
        }

        return false;

    }

    /**
     * isConnected 网络是否连接
     * 
     * @param context
     *            上下文
     * @return
     * 
     *         true:有网络 false :无网络
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo) {
                for (NetworkInfo info : networkInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
