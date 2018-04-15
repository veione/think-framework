package com.think.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * MD5工具类
 *
 * @author Gavin
 */
public final class MD5Util {

    public static String toMD5(String str) {
        Objects.requireNonNull(str);
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        byte[] input = str.getBytes();
        byte[] md5Buf = digest.digest(input);
        StringBuilder hexValue = new StringBuilder();
        for (byte value : md5Buf) {
            int val = value & 0xFF;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static String convertToMD5(String str) {
        char[] data = str.toCharArray();
        for (char ch : data) {
            ch = ((char) (ch ^ 0x74));
        }
        return new String(data);
    }

    public static void main(String[] args) {
        String str = "20b75697d8bf931a6730662ae117c3bf";
        System.out.println("原始 = [" + str + "]");
        System.out.println("MD5后 = [" + toMD5(str) + "]");
        System.out.println("加密的 = [" + convertToMD5(str) + "]");
        System.out.println("解密的 = [" + convertToMD5(convertToMD5(str)) + "]");
    }
}
