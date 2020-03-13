package com.neuifo.data.domain.utils;

import android.text.TextUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    private AESUtils() {
    }

    private static String aesPublicKey;

    // private static boolean flagEncypt = false;

    public static void initAes(String key) {
        aesPublicKey = key;
    }

    //private static final String PASSWORD = "46EBA22EF5204DD5B110A1F730513965"; // 加密秘钥

    public static boolean needReInit() {
        return TextUtils.isEmpty(aesPublicKey);
    }

    /*public static void setFlagEncypt(boolean flagEncypt) {
        AESUtils.flagEncypt = flagEncypt;
    }*/

    /*public static boolean isFlagEncypt() {
        return flagEncypt;
    }*/

    /**
     * 加密
     */
    public static String encrypt(String data) {
        /*if (!flagEncypt) {
            return data;
        }*/
        if (data == null || data.isEmpty()) {
            return data;
        }
        if (aesPublicKey == null || aesPublicKey.isEmpty()) {
            return data;
        }
        SecretKey secretKey = new SecretKeySpec(aesPublicKey.getBytes(), "AES"); // 恢复秘钥
        Cipher cipher = null;
        byte[] cipherBytes = null;
        try {
            cipher = Cipher.getInstance("AES"); // 对Cipher完成加密或解密工作
            cipher.init(Cipher.ENCRYPT_MODE, secretKey); // 对Cipher初始化,加密模式
            cipherBytes = cipher.doFinal(data.getBytes()); // 加密数据
            return Base64Util.encode(cipherBytes);
        } catch (Exception e) {

        }

        if (cipherBytes == null) {
            return data;
        }

        return new String(cipherBytes);

    }

    /**
     * 解密
     */
    public static String decrypt(String data) {
        /*if (!flagEncypt) {
            return data;
        }*/
        if (data == null || data.isEmpty()) {
            return data;
        }
        if (aesPublicKey == null || aesPublicKey.isEmpty()) {
            return data;
        }
        SecretKey secretKey = new SecretKeySpec(aesPublicKey.getBytes(), "AES"); // 恢复秘钥
        Cipher cipher = null;
        byte[] plainBytes = null;

        try {
            byte[] decode = Base64Util.decode(data);
            cipher = Cipher.getInstance("AES"); // 对Cipher初始化,加密模式
            cipher.init(Cipher.DECRYPT_MODE, secretKey); // 对Cipher初始化,加密模式
            plainBytes = cipher.doFinal(decode); // 解密数据
        } catch (Exception e) {
        }

        if (plainBytes == null) {
            return data;
        }

        return new String(plainBytes);

    }

}
