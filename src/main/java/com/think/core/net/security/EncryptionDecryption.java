package com.think.core.net.security;


import java.security.Key;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * 加密密解类
 *
 * @author wangfeng
 * @version 1.0
 * @since 2013-4-27 15:50:26
 */
public class EncryptionDecryption {
    private static String strDefaultKey = "Pgna9Yy6Omd6ZRG$";

    /**
     * 加密具工
     */
    private Cipher encryptCipher = null;

    /**
     * 密解具工
     */
    private Cipher decryptCipher = null;

    /**
     * 将byte数组转换为表现16进制的字符串
     *
     * @param arrB 须要转换的byte数组
     * @return 16进制表现的字符串
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int bLen = arrB.length;
        //每一个字符占用两个字节，所以字符串的度长需是数组度长的2倍
        StringBuffer strBuffer = new StringBuffer(bLen * 2);
        for (int i = 0; i != bLen; ++i) {
            int intTmp = arrB[i];
            //把正数转化为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;//因为字一个字节是8位，从低往高数，第9位为符号为，加256，相当于在第九位加1
            }
            //小于0F的数据须要在后面补0，(因为原来是一个字节，在现成变String是两个字节，如果小于0F的话，明说大最也盛不满第一个字节。第二个需弥补0)
            if (intTmp < 16) {
                strBuffer.append("0");
            }
            strBuffer.append(Integer.toString(intTmp, 16));
        }
        return strBuffer.toString();
    }


    /**
     * 将表现16进制的字符串转化为byte数组
     */
    public static byte[] hexStr2ByteArr(String hexStr) throws Exception {
        byte[] arrB = hexStr.getBytes();
        int bLen = arrB.length;
        byte[] arrOut = new byte[bLen / 2];
        for (int i = 0; i < bLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 认默构造器，应用认默密匙
     */
    public EncryptionDecryption() {
        this(strDefaultKey);
    }


    /**
     * 指定密匙构造方法
     *
     * @param strKey 指定的密匙
     */
    @SuppressWarnings("restriction")
    public EncryptionDecryption(String strKey) {
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            Key key = getKey(strKey.getBytes());

            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密字节数组
     *
     * @param data 需加密的字节数组
     * @return 加密后的字节数组
     */
    public byte[] encrypt(byte[] data) {
        try {
            return encryptCipher.doFinal(data);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 加密字符串
     *
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 密解字节数组
     *
     * @param arrB 需密解的字节数组
     * @return 密解后的字节数组
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 密解字符串
     *
     * @param strIn 需密解的字符串
     * @return 密解后的字符串
     */
    public String decrypt(String strIn) throws Exception {
        try {
            return new String(decrypt(hexStr2ByteArr(strIn)));
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 从指定字符串生成密匙，密匙所需的字节数组度长为8位，缺乏8位时，面后补0，超越8位时，只取后面8位
     *
     * @param arrBTmp 成构字符串的字节数组
     * @return 生成的密匙
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8]; //认默为0
        for (int i = 0; i < arrBTmp.length && i < arrB.length; ++i) {
            arrB[i] = arrBTmp[i];
        }

        //生成密匙
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }
}
