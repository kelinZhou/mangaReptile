package com.neuifo.mangareptile.event


import android.net.ConnectivityManager
import com.neuifo.mangareptile.annotation.NetworkType


object EventHolder {


    class NetworkEvent(
        @param:NetworkType @field:NetworkType
        @get:NetworkType
        val networkType: Int,
        /**
         * 判断此次事件是否为连接成功的事件。
         *
         * @return 如果为连接成功的事件则返回true，如果是断开连接的事件则返回false。
         */
        val isConnect: Boolean
    ) : EventBus.BusEvent {
        companion object {
            /**
             * WIFI
             */
            val NETWORK_TYPE_WIFI = ConnectivityManager.TYPE_WIFI
            /**
             * 移动流量
             */
            val NETWORK_TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE
        }
    }
}
