package com.kzksmarthome.common.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.kzksmarthome.common.module.log.L;

public final class MD5Util {
    private static final String CHARSET = "UTF-8";

    private MD5Util() {

    }

    /**
     * Returns a MessageDigest for the given <code>algorithm</code>.
     * 
     * @param algorithm The MessageDigest algorithm name.
     * @return An MD5 digest instance.
     * @throws ServiceException when a {@link java.security.NoSuchAlgorithmException} is caught
     */

    static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不存在", e);
        }
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
     * 
     * @param data Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(byte[] data) {
        return getDigest().digest(data);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
     * 
     * @param data Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(String data) throws Exception {
        return md5(data.getBytes(CHARSET));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     * 
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(byte[] data) {
        return HexUtil.toHexString(md5(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     * 
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(String data) {
        String result = null;
        try {
            result = HexUtil.toHexString(md5(data));
        } catch (Exception e) {
        }
        return result;
    }

    public static String md5File(String filename) {
        try {
            InputStream fis = new FileInputStream(filename);
            return md5Hex(fis);
        } catch (Exception e) {
            return null;
        }
    }

    public static String md5Hex(InputStream fis) {
        byte[] buffer = new byte[1024];
        int numRead = 0;
        try {
            MessageDigest md5 = getDigest();
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
            return HexUtil.toHexString(md5.digest());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getBytesMd5(byte[] bytes) {
        String byresMd5 = null;
        try {
            ByteBuffer byteBuffer = MappedByteBuffer.wrap(bytes);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bigInteger = new BigInteger(1, md5.digest());
            // byresMd5 = bigInteger.toString(16);
            byresMd5 = bufferToHex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            L.e(e);
        }
        return byresMd5;
    }

    public static String getFileMd5(File file) throws FileNotFoundException {
        String strFileMd5 = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0,
                    file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bigInteger = new BigInteger(1, md5.digest());
            // strFileMd5 = bigInteger.toString(16);
            strFileMd5 = bufferToHex(md5.digest());
        } catch (Exception e) {
            L.e(e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    L.e(e);
                }
            }
        }

        return strFileMd5;
    }

    final private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };

    private static String bufferToHex(byte bytes[], int start, int length) {
        StringBuffer strBuffer = new StringBuffer(2 * length);
        int k = start + length;
        for (int i = start; i < k; i++) {
            char c0 = hexDigits[(bytes[i] & 0xf0) >> 4];
            char c1 = hexDigits[bytes[i] & 0xf];
            strBuffer.append(c0);
            strBuffer.append(c1);
        }

        return strBuffer.toString();
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }
}
