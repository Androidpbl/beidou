package frame.util;

import java.security.MessageDigest;

public class MD5 {

	
	public static String md5(String s) {
		return md5(s,false);
	}
	
	/**
	 * md5加密
	 * @param flag 大小写
	 * @return
	 */
	public static String md5(String s,boolean flag) {
		if(s==null)return "";
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes("utf-8"));
			byte messageDigest[] = digest.digest();

			String str=toHexString(messageDigest);
			if(flag==true){
				str=str.toUpperCase();
			}
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}
	
}
