package com.neuifo.mangareptile

import androidx.multidex.MultiDexApplication
import com.hw.ycshareelement.YcShareElement
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.mangareptile.data.core.AppModule


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
        YcShareElement.enableContentTransition(this);
        LogHelper.init(BuildConfig.DEVELOPER_NAME, BuildConfig.DEBUG, true)
    }

    private fun handError() {
        AppLayerErrorCatcher.catchError {
            it.printStackTrace()
        }
    }

}
