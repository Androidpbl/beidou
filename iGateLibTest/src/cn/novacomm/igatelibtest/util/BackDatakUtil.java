package cn.novacomm.igatelibtest.util;

/**
 * @项目名:TestJava
 * 
 * @类名:BackUtil.java
 * 
 * @创建人:pangerwei
 * 
 * @类描述: 获取返回值类型
 * 
 * @date:2015-7-6
 * 
 * @Version:1.0
 */
public class BackDatakUtil {

	public static String action = "com.lfsfpew.receiver";

	/**
	 * 获取返回值类型
	 * 
	 * @param string
	 * @return
	 */
	public static String getBackType(byte[] b) {
		String result = "";
		if (null != b && b.length >= 3) {
			result = Integer.toHexString(b[1]) + Integer.toHexString(b[2]);
		}
		return Util.hexToStringGBK(result);
	}

	/**
	 * 获取短信内容 返回 16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String getBackContent(byte[] b, int cu) {
		String result = "";
		if (null != b && b.length >= 6) {
			if (cu == 0) {
//				result = Integer.toHexString(b[b.length - 2] & 0xFF);
				String hex = Integer.toHexString(b[b.length - 2] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				result = hex.toUpperCase();
			} else {
				for (int i = 6; i < b.length - 1; i++) {
					// result += Integer.toHexString(b[i] & 0xFF);
//					result = Integer.toHexString(b[b.length - 2] & 0xFF);
					String hex = Integer.toHexString(b[i] & 0xFF);
					if (hex.length() == 1) {
						hex = '0' + hex;
					}
					result += hex.toUpperCase();
				}
			}
		}
		return result;
	}

	/**
	 * 获取短信总包数
	 * 
	 * @param b
	 * @return
	 */
	public static int getTotalBao(byte[] b) {
		if (null != b) {
			return b[4];
		}
		return 0;
	}

	/**
	 * 获取当前是第几包
	 * 
	 * @param b
	 * @return
	 */
	public static int getCurrentBao(byte[] b) {
		if (null != b) {
			return b[5];
		}
		return 0;
	}

	/**
	 * 获取发件人
	 * 
	 * @param b
	 * @return
	 */
	public static String getSender(byte[] b) {
		if (null != b) {
			byte[] bytes = new byte[5];
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = b[13 + i];
			}
			String reString = String.valueOf(Long.parseLong(
					Encoding.bytes2HexString(bytes), 16));
			return reString;
		}
		return "";
	}

	/**
	 * 获取发送时间
	 * 
	 * @param b
	 * @return
	 */
	public static String getSendDate(byte[] b) {
		if (null != b) {
			String reString = String.valueOf(b[7] + 2000) + "/"
					+ String.valueOf(b[8] - 1) + "/" + String.valueOf(b[9])
					+ " " + String.valueOf(b[10]) + ":" + String.valueOf(b[11])
					+ ":" + String.valueOf(b[12]) + ":";
			return reString;
		}
		return "";
	}
}
