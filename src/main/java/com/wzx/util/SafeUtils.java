package com.wzx.util;

import com.google.common.base.Strings;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author admin
 * @version 1.0
 * @since 2019/12/5
 */
public class SafeUtils {

    public static String SHA1(String plainText) throws NoSuchAlgorithmException {
        String result = null;
        if (!Strings.isNullOrEmpty(plainText)) {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(plainText.getBytes());
            byte[] byteBuffer = messageDigest.digest();
            StringBuilder strHexString = new StringBuilder();

            for(int i = 0; i < byteBuffer.length; ++i) {
                String hex = Integer.toHexString(255 & byteBuffer[i]);
                if (hex.length() == 1) {
                    strHexString.append('0');
                }

                strHexString.append(hex);
            }

            result = strHexString.toString();
        }

        return result;
    }

    public static String MD5(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageByte = plainText.getBytes();
        byte[] md5Byte = md.digest(messageByte);

        StringBuilder hexStr = new StringBuilder();
        for(int i = 0; i < md5Byte.length; ++i) {
            int num = md5Byte[i];
            if (num < 0) {
                num += 256;
            }

            if (num < 16) {
                hexStr.append("0");
            }

            hexStr.append(Integer.toHexString(num));
        }

        return hexStr.toString().toUpperCase();

    }

}
