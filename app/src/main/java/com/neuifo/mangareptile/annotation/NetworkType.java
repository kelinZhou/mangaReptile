package com.neuifo.mangareptile.annotation;

import android.net.ConnectivityManager;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * **描述:** 网络事件的类型。
 * <p>
 * **创建人:** kelin
 * <p>
 * **创建时间:** 2019/3/28  4:56 PM
 * <p>
 * **版本:** v 1.0.0
 */
@IntDef({NetworkType.NETWORK_TYPE_WIFI, NetworkType.NETWORK_TYPE_MOBILE})
@Retention(RetentionPolicy.SOURCE)
public @interface NetworkType {
    /**
     * WIFI
     */
    int NETWORK_TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
    /**
     * 移动流量
     */
    int NETWORK_TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
}
