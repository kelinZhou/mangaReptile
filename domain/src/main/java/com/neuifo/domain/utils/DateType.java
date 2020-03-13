package com.neuifo.domain.utils;

/**
 * Created by neuifo on 2017/8/8.
 */

public enum DateType {

    DEFAULT_DATE_FORMAT("yyyy-MM-dd"),
    DATE_FORMAT_YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    DATE_FORMAT_YYYYMMDD_HHMMSS("yyyy-MM-dd HH:mm:ss"),
    DATE_FORMAT_HHMM("HH:mm"),
    DATE_FORMAT_MMDD("MMdd"),
    DATE_FORMAT_MM_DD_HH_MM("MM-dd HH:mm"),
    DATE_FORMAT_MMDD2("MM月dd日"),
    DATE_FORMAT_MMDDHHMM("MM-dd HH:mm"),
    DATE_FORMAT_MMDD1("MM-dd"),
    DATE_FORMAT_YYMM("yyMM"),
    DATE_FORMAT_YYMMDDHH("yyyy-MM-dd HH:mm"),
    DATE_FORMAT_YYMMDD("yyyy年MM月dd号"),
    DATE_FORMAT_CHAT("MM月dd日 / ahh:mm"),
    DATE_FORMAT_YY_MM_DD_HH_MM("yyyy年MM月dd号  HH:mm"),
    DATE_FORMAT_YY_MM_DD_HH_MM1("yyyy-MM-dd HH:mm"),
    REMIND_TIME_TYPE("yyyy-MM-dd    |    HH:mm"),
    DATE_FORMAT_YYYY_MM("yyyy年MM月"),
    DATE_FORMAT_YYMM2("yyyy-MM"),
    DATE_FORMAT_YYMM3("yy年MM"),
    DATE_FORMAT_YYMM4("yyyy.MM"),
    DATE_FORMAT_YY("yyyy"),
    DATE_FORMAT_DAY_TIME("[MM-dd HH:mm]"),
    DATE_FORMAT_12TIME("a hh:mm"),
    ;

    private String value;

    DateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
