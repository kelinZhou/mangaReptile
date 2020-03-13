package com.neuifo.mangareptile

import androidx.multidex.MultiDexApplication
import com.neuifo.data.domain.utils.LogHelper


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        LogHelper.init(BuildConfig.DEVELOPER_NAME, BuildConfig.DEBUG, true)
    }

    private fun handError() {
        AppLayerErrorCatcher.catchError {
            it.printStackTrace()
        }
    }

}
