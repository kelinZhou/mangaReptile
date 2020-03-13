package com.neuifo.domain.utils;


/**
 * Created by neuifo on 2017/6/19.
 */

public class StringHelper {

    public static void initStringBuffer(StringBuffer buffer, String data) {
        if (data != null && data.length() > 0) buffer.append(data.replace("null", ""));
    }

    public static String warpString(String data) {
        if (data == null || data.length() == 0) {
            return null;
        }
        return data;
    }

    public static String decodeColorString(String source, String color) {
        return decodeColorString(source, source, color);
    }

    public static String decodeColorString(String source, String regx, String color) {
        String part = "<font color=#" + color + ">" + regx + "</font>";
        return source.replace(regx, part);
    }

    public static String decodeSizeString(String source, String regx, int size) {
        String part = "<font size=" + size + ">" + regx + "</font>";
        return source.replace(regx, part);
    }

    public static String getStaredPhoneNumber(String mobile) {
        StringBuilder sb = new StringBuilder();
        if (mobile != null && mobile.length() > 6) {
            for (int i = 0; i < mobile.length(); i++) {
                char c = mobile.charAt(i);
                if (i > 2 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

}
