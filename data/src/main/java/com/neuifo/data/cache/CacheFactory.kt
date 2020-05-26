package com.neuifo.data.cache


import android.content.Context
import com.neuifo.data.cache.basic.ComicCache

class CacheFactory private constructor() {

    private var cache: ComicCache? = null

    val comicCache: ComicCache?
        get() = cache

    fun init(context: Context) {
        cache = ComicDetailCacheImpl(context)
    }

    companion object {
        val instance: CacheFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CacheFactory()
        }
    }
}
