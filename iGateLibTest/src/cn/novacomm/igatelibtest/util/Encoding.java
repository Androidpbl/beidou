package cn.novacomm.igatelibtest.util;

import java.io.UnsupportedEncodingException;

public class Encoding {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String gbkIsn = "B2E2CAD4";
		String content = "。";
		String result = gbk2Unicode(gbkIsn);
		String isn = unicode2Gbk(content);
		System.out.println(result);
		System.out.println(isn);
	}

	/**
	 * Created on 2010-04-16
	 * 
	 * @author yangguo
	 * @param isn
	 *            GBK �����ַ���
	 * @return
	 */
	public static String gbk2Unicode(String isn) {
		byte[] bytes = new byte[isn.length() / 2];
		for (int i = 0, j = 0; i < isn.length(); i += 2, j++) {
			bytes[j] = Integer.decode("0X" + isn.substring(i, i + 2))
					.byteValue();
		}
		try {
			return new String(bytes, "gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return isn;

		}
	}

	/**
	 * Created on 2010-04-16
	 * 
	 * @author yangguo
	 * @param content
	 *            Ҫת��������
	 * @return GBK����
	 */
	public static String unicode2Gbk(String content) {
		byte[] tmp;
		String result = "";
		try {
			tmp = content.getBytes("GBK");
			for (int i = 0; i < tmp.length; i++) {
				int value = tmp[i] + 256;
				result += "0x" + Integer.toHexString(value) + ",";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result.toUpperCase();
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
	
}
