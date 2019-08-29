package com.kzksmarthome.common.module.log;

import java.io.UnsupportedEncodingException;

/**
 * Rc4 加密
 * 
 * @author Tangbl
 */
public class GjjRc4 {
	/**
	 * 用于 RC4 处理密码
	 * 
	 * @param pass
	 *            密码字串
	 * @param kLen
	 *            密钥长度，一般为 256
	 * @return
	 */
	private static byte[] createKey(byte[] pass, int kLen) {
		byte[] mBox = new byte[kLen];

		for (int i = 0; i < kLen; i++) {
			mBox[i] = (byte) i;
		}

		int j = 0;
		for (int i = 0; i < kLen; i++) {

			j = (j + (int) ((mBox[i] + 256) % 256) + pass[i % pass.length]) % kLen;

			byte temp = mBox[i];
			mBox[i] = mBox[j];
			mBox[j] = temp;
		}

		return mBox;
	}

	/**
	 * RC4加密
	 * 
	 * @param input
	 *            需要加密的数据
	 * @param pass
	 *            密钥
	 * @return 加密数据
	 */
	public static byte[] Rc4(byte[] input, String pass) {
		if (input == null || pass == null)
			return null;

		byte[] output = new byte[input.length];
		byte[] mBox = createKey(pass.getBytes(), 256);

		// 加密
		int i = 0;
		int j = 0;

		for (int offset = 0; offset < input.length; offset++) {
			i = (i + 1) % mBox.length;
			j = (j + (int) ((mBox[i] + 256) % 256)) % mBox.length;

			byte temp = mBox[i];
			mBox[i] = mBox[j];
			mBox[j] = temp;
			byte a = input[offset];

			// byte b = mBox[(mBox[i] + mBox[j] % mBox.Length) % mBox.Length];
			// mBox[j] 一定比 mBox.Length 小，不需要在取模
			byte b = mBox[(toInt(mBox[i]) + toInt(mBox[j])) % mBox.length];

			output[offset] = (byte) ((int) a ^ (int) toInt(b));
		}

		return output;
	}

	/**
	 * 加密先转byte然后进行加密，再转16进制
	 * 
	 * @param str
	 * @return
	 */
	public static String rc4HexOut(String str, String key) {
		try {
			byte[] in = str.getBytes("UTF-8");
			// byte[] out = Rc4(in, KcUserConfig.getDataString(KcApplication.getContext(), KcUserConfig.JKey_Key));
			byte[] out = Rc4(in, key);

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < out.length; i++) {
				String s4 = Integer.toHexString(out[i]);
				s4 = "00" + s4;
				s4 = s4.substring(s4.length() - 2, s4.length());
				sb.append(s4);
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			L.e(e);
		}
		return null;
	}

	public static int toInt(byte b) {
		return (int) ((b + 256) % 256);
	}

	public static void printArray(String tag, byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(String.format("%02x,", array[i]));
		}
		L.d(tag + ":" + sb.toString());
	}

	/**
	 * rc4 算法(加解密适用)
	 * 
	 * @param str
	 * @return
	 * @author: 龙小龙
	 * @version: 2011-9-21 下午02:59:59
	 */
	public static String rc4(String str) {
		byte[] in = HexString2Bytes(str);
		byte[] out = Rc4(in, LogUpLoad.password_key);
		try {
			return new String(out, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			L.e(e);
		}
		return null;
	}

	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
	 * 
	 * @param src
	 *            String
	 * @return byte[]
	 */
	public static byte[] HexString2Bytes(String src) {
		int max = src.length() / 2;
		byte[] ret = new byte[max];
		for (int i = 0; i < max; i++) {
			int b = Integer.parseInt(src.substring(i * 2, i * 2 + 2), 16);
			ret[i] = (byte) (b & 0xFF);
		}
		return ret;
	}

	/**
	 * RC4解密
	 * 
	 * @param key
	 *            : key
	 * @param data
	 *            : 要解密的数组
	 * @return
	 * @author: kevin
	 * @version: 2012-7-5 下午03:59:00
	 */
	public static String decry_RC4(byte[] data, String key) {
		if (data == null || key == null) {
			return null;
		}
		return asString(RC4Base(data, key));
	}

	/**
	 * RC4解密算法
	 * 
	 * @param data
	 *            需要解密字符串
	 * @param key
	 *            密钥
	 * @return
	 * @author: Kevin
	 * @version: 2011-12-7 下午03:06:33
	 */
	public static String decry_RC4(String data, String key) {
		if (data == null || key == null) {
			return null;
		}
		return new String(RC4Base(HexString2Bytes(data), key));
	}

	/**
	 * RC4加密
	 * 
	 * @param key
	 *            :key
	 * @param data
	 *            :要加密的字符串
	 * @return
	 * @author: xiaozhenhua
	 * @version: 2012-7-5 下午03:59:00
	 */
	public static byte[] encry_RC4_byte(String data, String key) {
		if (data == null || key == null) {
			return null;
		}
		byte b_data[] = data.getBytes();
		return RC4Base(b_data, key);
	}

	/**
	 * RC4加密
	 * 
	 * @param key
	 *            :key
	 * @param data
	 *            :要加密的字符串
	 * @return
	 * @author: xiaozhenhua
	 * @version: 2012-7-5 下午03:59:00
	 */
	public static String encry_RC4_string(String data, String key) {
		if (data == null || key == null) {
			return null;
		}
		return toHexString(asString(encry_RC4_byte(data, key)));
	}

	/**
	 * 
	 * 转换成String
	 * 
	 * @param buf
	 * @return
	 * @author: xiaozhenhua
	 * @version: 2012-7-5 下午03:58:30
	 */
	private static String asString(byte[] buf) {
		StringBuffer strbuf = new StringBuffer(buf.length);
		for (int i = 0; i < buf.length; i++) {
			strbuf.append((char) buf[i]);
		}
		return strbuf.toString();
	}

	private static byte[] initKey(String aKey) {
		byte[] b_key = aKey.getBytes();
		byte state[] = new byte[256];

		for (int i = 0; i < 256; i++) {
			state[i] = (byte) i;
		}
		int index1 = 0;
		int index2 = 0;
		if (b_key == null || b_key.length == 0) {
			return null;
		}
		for (int i = 0; i < 256; i++) {
			index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
			byte tmp = state[i];
			state[i] = state[index2];
			state[index2] = tmp;
			index1 = (index1 + 1) % b_key.length;
		}
		return state;
	}

	private static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch & 0xFF);
			if (s4.length() == 1) {
				s4 = '0' + s4;
			}
			str = str + s4;
		}
		return str;// 0x表示十六进制
	}

//	private static byte uniteBytes(byte src0, byte src1) {
//		char _b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
//		_b0 = (char) (_b0 << 4);
//		char _b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
//		byte ret = (byte) (_b0 ^ _b1);
//		return ret;
//	}

	private static byte[] RC4Base(byte[] input, String mKkey) {
		int x = 0;
		int y = 0;
		byte key[] = initKey(mKkey);
		int xorIndex;
		byte[] result = new byte[input.length];

		for (int i = 0; i < input.length; i++) {
			x = (x + 1) & 0xff;
			y = ((key[x] & 0xff) + y) & 0xff;
			byte tmp = key[x];
			key[x] = key[y];
			key[y] = tmp;
			xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
			result[i] = (byte) (input[i] ^ key[xorIndex]);
		}
		return result;
	}
}
