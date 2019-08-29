package com.project.netty.util;

public class StringUtils {
	 
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
	
	public static final String bytesToHexStringJoinDelimiter(byte[] bArray,
			String delimiter) {

		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		int tmp = 0;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
			tmp ++;
			if(tmp%4 ==0) {
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}
	
	public static String byteToHexStr(byte b) {
		StringBuffer sb = new StringBuffer();
		String sTemp = Integer.toHexString(0xFF & b);
		if (sTemp.length() < 2) {
			sb.append(0);
		}
		sb.append(sTemp.toUpperCase());
		return sb.toString();
	}
	
}
