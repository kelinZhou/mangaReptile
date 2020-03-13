package com.neuifo.domain.utils;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * <strong>描述: </strong> 数字相关的工具类。
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/5/7  下午5:39
 * <p><strong>版本: </strong> v 1.0.0
 */

public abstract class Numbers {

    public static int parseInt(String s) {
        int i;
        try {
            i = Integer.parseInt(s.trim());
        } catch (Exception ignore) {
            i = 0;
        }
        return i;
    }

    public static long parseLong(String s) {
        long i;
        try {
            i = Long.parseLong(s.trim());
        } catch (Exception ignore) {
            i = 0;
        }
        return i;
    }

    public static double parseDouble(@NotNull String s) {
        double i;
        try {
            i = Double.parseDouble(s.trim());
        } catch (Exception ignore) {
            i = 0;
        }
        return i;
    }

    /**
     * 格式化除法运算。
     * @param divisor 除数。
     * @param dividend 被除数。
     * @return 返回运算后且已经被格式化后的数字字符串,如果为小数则返回最大两位的小数，如果为整数这返回整数。
     */
    public static String formatDivision(int divisor, int dividend) {
        return formatDivision(divisor, dividend, -1);
    }

    /**
     * 格式化除法运算。
     * @param divisor 除数。
     * @param dividend 被除数。
     * @return 返回运算后且已经被格式化后的数字字符串,如果为小数则返回最大两位的小数，如果为整数这返回整数。
     */
    public static String formatDivision(long divisor, long dividend) {
        return formatDivision(divisor, dividend, -1);
    }

    /**
     * 格式化除法运算。
     * @param divisor 除数。
     * @param dividend 被除数。
     * @return 返回运算后且已经被格式化后的数字字符串,如果为小数则返回最大两位的小数，如果为整数这返回整数。
     */
    public static String formatDivision(double divisor, double dividend) {
        return formatDivision(divisor, dividend, -1);
    }

    /**
     * 格式化除法运算。
     * @param divisor 除数。
     * @param dividend 被除数。
     * @param decimalDigits 要保留的小数的位数。
     * @return 返回运算后且已经被格式化后的数字字符串。
     */
    public static String formatDivision(double divisor, double dividend, int decimalDigits) {
        return format(divisor / dividend, decimalDigits);
    }

    /**
     * 格式化小数。
     * @param number 要格式化的小数。
     * @return 返回格式化后数字字符串,如果为小数则返回最大两位的小数，如果为整数这返回整数。
     */
    public static String format(double number) {
        return format(number, -1);
    }

    /**
     * 格式化小数。
     * @param number 要格式化的小数。
     * @param decimalDigits 要保留的小数的位数。
     * @return 返回格式化后数字字符串。
     */
    public static String format(double number, int decimalDigits) {
        if (decimalDigits > 0) {
            return String.format("%." + decimalDigits + "f", number);
        } else {
            return new DecimalFormat(decimalDigits < 0 ? "0.##" : "#").format(number);
        }
    }
}
