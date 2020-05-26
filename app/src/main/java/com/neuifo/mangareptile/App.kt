package com.neuifo.mangareptile

import androidx.multidex.MultiDexApplication
import com.hippo.image.Image
import com.hw.ycshareelement.YcShareElement
import com.neuifo.data.cache.CacheFactory
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.mangareptile.data.core.AppModule


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
        YcShareElement.enableContentTransition(this)
        Image.initialize(this)
        CacheFactory.instance.init(this)
        LogHelper.init(BuildConfig.DEVELOPER_NAME, BuildConfig.DEBUG, true)
    }

    private fun handError() {
        AppLayerErrorCatcher.catchError {
            it.printStackTrace()
        }
    }

}
