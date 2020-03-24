package org.siu.saku.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author Siu
 * @Date 2020/3/24 21:53
 * @Version 0.0.1
 */
public class MD5Util {

    static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /**
     * 可以把一段文字转换为MD
     * Can convert a file to MD5
     *
     * @param text
     * @return md5
     */
    public static String encode(String text) {
        byte[] buffer = digest.digest(text.getBytes());
        // byte -128 ---- 127
        StringBuffer sb = new StringBuffer();
        for (byte b : buffer) {
            int a = b & 0xff;
            // Log.d(TAG, "" + a);
            String hex = Integer.toHexString(a);

            if (hex.length() == 1) {
                hex = 0 + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /***
     * 任意文件转换成Md5
     * Can convert a text to MD5
     * @param in
     * @return md5
     */

    public static String encode(InputStream in) {
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[8192];
            int byteCount;
            while ((byteCount = in.read(bytes)) > 0) {
                digester.update(bytes, 0, byteCount);
            }
            byte[] digest = digester.digest();

            // byte -128 ---- 127
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                int a = b & 0xff;
                // Log.d(TAG, "" + a);

                String hex = Integer.toHexString(a);

                if (hex.length() == 1) {
                    hex = 0 + hex;
                }

                sb.append(hex);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
        }
        return null;
    }
}
