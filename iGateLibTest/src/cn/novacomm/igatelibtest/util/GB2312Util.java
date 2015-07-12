package cn.novacomm.igatelibtest.util;

public class GB2312Util {

	public static String bytes2HexString(byte b) {
		return bytes2HexString(new byte[] { b });
	}

	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	public static void main(String[] args) throws Exception {
		String str = "孙";
		byte[] bs = str.getBytes("GB2312");
		String s = "";
		for (int i = 0; i < bs.length; i++) {
			int a = Integer.parseInt(bytes2HexString(bs[i]), 16);
			s += (a - 0x80 - 0x20) + "";
		}
		System.out.println(s);
	}
}
