package frame.http;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

    // md5加密
    public static String md5(String inputText) {
        return encrypt(inputText, "md5");
    }

    // sha加密
    public static String sha(String inputText) {
        return encrypt(inputText, "sha-1");
    }

    // 加密算法
    // sha加密
    public static String shall(String inputText) {
        return encryptt(inputText);
    }

    /**
     * md5或者sha-1加密
     * 
     * @param inputText
     *            要加密的内容
     * @param algorithmName
     *            加密算法名称：md5或者sha-1，不区分大小写
     * @return
     */
    private static String encryptt(String inputText) {
        String md5 = md5(inputText);
        String sha = sha(inputText);
        String pws = sha + md5;
        pws = pws.substring(0, 9) + "s" + pws.substring(10, 19) + "h"
                + pws.substring(20, 29) + "l" + pws.substring(30, 39) + "s"
                + pws.substring(40, 49) + "u" + pws.substring(50, 59) + "n"
                + pws.substring(60, 69) + "y" + pws.substring(70, 72);
        pws = pws.substring(36, 72) + pws.substring(0, 36);
        pws = pws.substring(0, 70);
        pws = pws.substring(0, 14) + "j" + pws.substring(14, 28) + "x"
                + pws.substring(28, 42) + "q" + pws.substring(32, 46) + "y"
                + pws.substring(56, 70) + "3";
        // System.out.println("sha="+sha);
        pws = pws.substring(40, 75) + pws.substring(0, 40);
        return pws;
    }

    public static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes("UTF8"));
            byte s[] = m.digest();
            // m.digest(inputText.getBytes("UTF8"));
            return hex(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptText;
    }

    // 返回十六进制字符串
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
                    3));
        }
        return sb.toString();
    }

    public static void main(String args[]) {

        System.out.println(EncryptUtil.encryptt("666666"));

    }

}