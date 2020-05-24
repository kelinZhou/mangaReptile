package com.neuifo.mangareptile.ui.detail

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hippo.glgallery.GalleryProvider
import com.hippo.glgallery.GalleryView
import com.hippo.glgallery.SimpleAdapter
import com.hippo.glview.view.GLRootView
import com.neuifo.domain.model.base.WarpData
import com.neuifo.mangareptile.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

class ComicReadActivity : AppCompatActivity(),
    GalleryView.Listener {


    companion object {

        private val COMIC_ID = "COMIC_ID"
        private val CHAPTER_ID = "CHAPTER_ID"
        private val CHAPTERS = "CHAPTERS"


        fun setData(intent: Intent, comicId: Long, chapterId: Long, chapter: WarpData) {
            intent.putExtra(COMIC_ID, comicId)
            intent.putExtra(CHAPTER_ID, chapterId)
            intent.putExtra(CHAPTERS, chapter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        glRootView = findViewById<GLRootView>(R.id.gl_root_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setData(
                intent.getLongExtra(COMIC_ID, 0L),
                intent.getLongExtra(CHAPTER_ID, 0L),
                intent.getParcelableExtra(CHAPTERS)
            )
        }
    }


    lateinit var provider: GalleryProvider
    lateinit var glRootView: GLRootView

    @RequiresApi(Build.VERSION_CODES.M)
    fun setData(comicId: Long, startChapter: Long, chapter: WarpData) {
        provider = ComicProvider(comicId, startChapter, chapter)

        val adapter = GalleryAdapter(glRootView, provider)
        provider.setListener(adapter)
        provider.setGLRoot(glRootView)


        val galleryView = GalleryView.Builder(this, adapter)
            .setListener(this)
            .setLayoutMode(GalleryView.LAYOUT_TOP_TO_BOTTOM)
            .setScaleMode(GalleryView.SCALE_FIT)
            .setStartPosition(GalleryView.START_POSITION_TOP_RIGHT)
            .setStartPage(0)
            .setBackgroundColor(getColor(R.color.colorPrimary))
            .setEdgeColor(getColor(R.color.grey_3300000))
            .setPagerInterval(resources.getDimensionPixelOffset(R.dimen.common_button_height))
            .setScrollInterval(resources.getDimensionPixelOffset(R.dimen.common_button_height))
            .setPageMinHeight(resources.getDimensionPixelOffset(R.dimen.page_min_height))
            .setPageInfoInterval(resources.getDimensionPixelOffset(R.dimen.page_info_interval))
            .setProgressColor(getColor(R.color.white))
            .setProgressSize(resources.getDimensionPixelOffset(R.dimen.common_button_height))
            .setPageTextColor(getColor(R.color.white))
            .setPageTextSize(resources.getDimensionPixelOffset(R.dimen.page_text_size))
            .setPageTextTypeface(Typeface.DEFAULT)
            .setErrorTextColor(getColor(R.color.red_f4333c))
            .setErrorTextSize(resources.getDimensionPixelOffset(R.dimen.error_text_size))
            .setDefaultErrorString(getString(R.string.loaded_failed_click_to_retry))
            .setEmptyString(getString(R.string.no_data))
            .build()
        glRootView.setContentPane(galleryView)
    }


    override fun onPause() {
        super.onPause()
        glRootView.onPause()
        provider.stop()
    }

    override fun onResume() {
        super.onResume()
        glRootView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        provider.setListener(null)
        provider.stop()
    }


    private class GalleryAdapter(glRootView: GLRootView, provider: GalleryProvider) :
        SimpleAdapter(glRootView, provider) {
        override fun onDataChanged() {
            super.onDataChanged()

        }
    }

    override fun onLongPressPage(index: Int) {
    }

    override fun onTapSliderArea() {
    }

    override fun onTapMenuArea() {
    }

    override fun onUpdateCurrentIndex(index: Int) {
    }
}