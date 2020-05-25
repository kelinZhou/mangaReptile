package com.neuifo.mangareptile.ui.detail

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.hippo.glgallery.GalleryProvider
import com.hippo.glgallery.GalleryView
import com.hippo.glgallery.SimpleAdapter
import com.hippo.glview.view.GLRootView
import com.hippo.yorozuya.AnimationUtils
import com.hippo.yorozuya.MathUtils
import com.hippo.yorozuya.SimpleAnimatorListener
import com.hippo.yorozuya.SimpleHandler
import com.neuifo.domain.model.base.WarpData
import com.neuifo.domain.model.dmzj.Chapter
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.data.core.API
import com.neuifo.mangareptile.data.proxy.ProxyFactory
import com.neuifo.mangareptile.utils.Settings
import com.neuifo.mangareptile.utils.StyleHelper
import com.neuifo.mangareptile.utils.ToastUtil
import com.neuifo.mangareptile.utils.getVisiable
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.activity_gallery.progress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ContainerOptions(cache = CacheImplementation.NO_CACHE)
class ComicReadActivity : AppCompatActivity(),
    GalleryView.Listener {


    companion object {

        private val COMIC_ID = "COMIC_ID"
        private val CHAPTER_ID = "CHAPTER_ID"
        private val CHAPTERS = "CHAPTERS"
        private const val SLIDER_ANIMATION_DURING: Long = 150
        private const val HIDE_SLIDER_DELAY: Long = 3000


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

        ProxyFactory.createIdProxy<Long, Chapter> { id ->
            API.DMZJ_Dmzj.getChapter(intent.getLongExtra(COMIC_ID, 0L), id)
        }.onSuccess { comicId, chapter ->
            setData(
                comicId,
                chapter,
                intent.getParcelableExtra(CHAPTERS)
            )
        }.onFailed { _, e ->
            ToastUtil.showAlertToast(e.displayMessage)
        }.request(intent.getLongExtra(CHAPTER_ID, 0L))

    }


    lateinit var provider: GalleryProvider
    lateinit var glRootView: GLRootView
    lateinit var galleryView: GalleryView

    private fun setData(comicId: Long, startChapter: Chapter, chapter: WarpData) {
        provider = ComicProvider(comicId, startChapter, chapter)
        provider.start()
        val adapter = GalleryAdapter(glRootView, provider)
        provider.setListener(adapter)
        provider.setGLRoot(glRootView)
        right.text = "${startChapter.pageNums}"

        galleryView = GalleryView.Builder(this, adapter)
            .setListener(this)
            .setLayoutMode(Settings.getReadingDirection())
            .setScaleMode(Settings.getPageScaling())
            .setStartPosition(Settings.getStartPosition())
            .setStartPage(0)
            .setBackgroundColor(resources.getColor(R.color.colorPrimary))
            .setEdgeColor(resources.getColor(R.color.grey_3300000))
            .setPagerInterval(
                if (Settings.getShowPageInterval()) resources.getDimensionPixelOffset(
                    R.dimen.common_button_height
                ) else 0
            )
            .setScrollInterval(
                if (Settings.getShowPageInterval()) resources.getDimensionPixelOffset(
                    R.dimen.common_button_height
                ) else 0
            )
            .setPageMinHeight(resources.getDimensionPixelOffset(R.dimen.page_min_height))
            .setPageInfoInterval(resources.getDimensionPixelOffset(R.dimen.page_info_interval))
            .setProgressColor(resources.getColor(R.color.white))
            .setProgressSize(resources.getDimensionPixelOffset(R.dimen.common_button_height))
            .setPageTextColor(resources.getColor(R.color.white))
            .setPageTextSize(resources.getDimensionPixelOffset(R.dimen.page_text_size))
            .setPageTextTypeface(Typeface.DEFAULT)
            .setErrorTextColor(resources.getColor(R.color.red_f4333c))
            .setErrorTextSize(resources.getDimensionPixelOffset(R.dimen.error_text_size))
            .setDefaultErrorString(getString(R.string.loaded_failed_click_to_retry))
            .setEmptyString(getString(R.string.no_data))
            .build()

        glRootView.setContentPane(galleryView)

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                left.text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val currentPage = seekBar?.progress ?: 0
                galleryView.setCurrentPage(currentPage)
            }
        })
        seek_bar.max = startChapter.pageNums.toInt()
        seek_bar.progress = 0

        // Update keep screen on
        if (Settings.getKeepScreenOn()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        // Orientation
        // Orientation
        requestedOrientation = when (Settings.getScreenRotation()) {
            0 -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            1 -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            2 -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            3 -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
            else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        // Screen lightness
        setScreenLightness(Settings.getCustomScreenLightness(), Settings.getScreenLightness())

        // Cutout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            gallery_header.setOnApplyWindowInsetsListener { _, insets ->
                gallery_header.setDisplayCutout(insets.displayCutout)
                insets
            }
        }
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

    private fun setScreenLightness(enable: Boolean, lightness: Int) {
        var lightness = lightness
        val w = window
        val lp = w.attributes
        if (enable) {
            lightness = MathUtils.clamp(lightness, 0, 200)
            if (lightness > 100) {
                mask.setColor(0)
                // Avoid BRIGHTNESS_OVERRIDE_OFF,
                // screen may be off when lp.screenBrightness is 0.0f
                lp.screenBrightness = Math.max((lightness - 100) / 100.0f, 0.01f)
            } else {
                mask.setColor(
                    MathUtils.lerp(
                        0xde,
                        0x00,
                        lightness / 100.0f
                    ) shl 24
                )
                lp.screenBrightness = 0.01f
            }
        } else {
            mask.setColor(0)
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        }
        w.attributes = lp
    }


    private var mSeekBarPanelAnimator: ObjectAnimator? = null

    private val mUpdateSliderListener =
        ValueAnimator.AnimatorUpdateListener {
            seek_bar_panel.requestLayout()
        }

    private val mShowSliderListener: SimpleAnimatorListener = object : SimpleAnimatorListener() {
        override fun onAnimationEnd(animation: Animator) {
            mSeekBarPanelAnimator = null
        }
    }

    private val mHideSliderListener: SimpleAnimatorListener = object : SimpleAnimatorListener() {
        override fun onAnimationEnd(animation: Animator) {
            mSeekBarPanelAnimator = null
            seek_bar_panel.visibility = View.INVISIBLE
        }
    }

    private val mHideSliderRunnable = Runnable {
        hideSlider(seek_bar_panel)
    }

    private fun showSlider(sliderPanel: View) {
        if (null != mSeekBarPanelAnimator) {
            mSeekBarPanelAnimator?.cancel()
            mSeekBarPanelAnimator = null
        }
        seek_bar.progress = galleryView.currentIndex
        left.text = "${galleryView.currentIndex}"


        sliderPanel.translationY = sliderPanel.height.toFloat()
        sliderPanel.visibility = View.VISIBLE
        mSeekBarPanelAnimator =
            ObjectAnimator.ofFloat(sliderPanel, "translationY", 0.0f)
        mSeekBarPanelAnimator?.setDuration(SLIDER_ANIMATION_DURING)
        mSeekBarPanelAnimator?.setInterpolator(AnimationUtils.FAST_SLOW_INTERPOLATOR)
        mSeekBarPanelAnimator?.addUpdateListener(mUpdateSliderListener)
        mSeekBarPanelAnimator?.addListener(mShowSliderListener)
        mSeekBarPanelAnimator?.start()
    }

    private fun hideSlider(sliderPanel: View) {
        if (null != mSeekBarPanelAnimator) {
            mSeekBarPanelAnimator!!.cancel()
            mSeekBarPanelAnimator = null
        }
        mSeekBarPanelAnimator = ObjectAnimator.ofFloat(
            sliderPanel,
            "translationY",
            sliderPanel.height.toFloat()
        )
        mSeekBarPanelAnimator?.setDuration(SLIDER_ANIMATION_DURING)
        mSeekBarPanelAnimator?.setInterpolator(AnimationUtils.SLOW_FAST_INTERPOLATOR)
        mSeekBarPanelAnimator?.addUpdateListener(mUpdateSliderListener)
        mSeekBarPanelAnimator?.addListener(mHideSliderListener)
        mSeekBarPanelAnimator?.start()
    }


    private class GalleryAdapter(glRootView: GLRootView, provider: GalleryProvider) :
        SimpleAdapter(glRootView, provider)

    override fun onLongPressPage(index: Int) {
        provider.forceRequest(index)
    }

    override fun onTapSliderArea() {
        GlobalScope.launch(Dispatchers.Main) {
            if (seek_bar_panel.visibility == View.VISIBLE) {
                hideSlider(seek_bar_panel)
            } else {
                showSlider(seek_bar_panel)
                SimpleHandler.getInstance().postDelayed(
                    mHideSliderRunnable,
                    HIDE_SLIDER_DELAY
                )
            }
        }
    }

    override fun onTapMenuArea() {
        GlobalScope.launch(Dispatchers.Main) {
            StyleHelper.showComicHelpDialog(this@ComicReadActivity) {
                requestedOrientation = Settings.getScreenRotation()

                galleryView.layoutMode = Settings.getReadingDirection()
                galleryView.setScaleMode(Settings.getPageScaling())
                galleryView.setStartPosition(Settings.getStartPosition())
                if (Settings.getKeepScreenOn()) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
                clock.visibility = Settings.getShowClock().getVisiable()
                progress.visibility = Settings.getShowProgress().getVisiable()

                battery.visibility = Settings.getShowBattery().getVisiable()
                galleryView.setPagerInterval(
                    if (Settings.getShowPageInterval()) resources.getDimensionPixelOffset(
                        R.dimen.common_button_height
                    ) else 0
                )
                galleryView.setScrollInterval(
                    if (Settings.getShowPageInterval()) resources.getDimensionPixelOffset(
                        R.dimen.common_button_height
                    ) else 0
                )

            }
        }
    }

    override fun onUpdateCurrentIndex(index: Int) {

    }

    override fun onTapErrorText(index: Int) {
        provider.forceRequest(index)
    }
}