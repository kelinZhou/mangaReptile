package com.neuifo.mangareptile.utils

import com.hippo.glgallery.GalleryView
import com.hippo.glgallery.GalleryView.ScaleMode
import com.hippo.glgallery.GalleryView.StartPosition
import com.neuifo.data.util.sp.SpUtil
import com.neuifo.mangareptile.data.core.AppModule

object Settings {

    private const val KEY_SCREEN_ROTATION = "screen_rotation"
    private const val DEFAULT_SCREEN_ROTATION = 0
    private const val KEY_READING_DIRECTION = "reading_direction"
    private const val DEFAULT_READING_DIRECTION = GalleryView.LAYOUT_TOP_TO_BOTTOM

    private const val KEY_KEEP_SCREEN_ON = "keep_screen_on"
    private const val DEFAULT_KEEP_SCREEN_ON = false

    private const val KEY_START_POSITION = "start_position"
    private const val DEFAULT_START_POSITION = GalleryView.START_POSITION_TOP_RIGHT

    private const val KEY_SHOW_CLOCK = "gallery_show_clock"
    private const val DEFAULT_SHOW_CLOCK = true

    private const val KEY_SHOW_PROGRESS = "gallery_show_progress"
    private const val DEFAULT_SHOW_PROGRESS = true

    private const val KEY_SHOW_BATTERY = "gallery_show_battery"
    private const val DEFAULT_SHOW_BATTERY = true

    private const val KEY_SHOW_PAGE_INTERVAL = "gallery_show_page_interval"
    private const val DEFAULT_SHOW_PAGE_INTERVAL = true

    private const val KEY_PAGE_SCALING = "page_scaling"
    private const val DEFAULT_PAGE_SCALING = GalleryView.SCALE_FIT

    private const val KEY_VOLUME_PAGE = "volume_page"
    private const val DEFAULT_VOLUME_PAGE = false

    private const val KEY_CUSTOM_SCREEN_LIGHTNESS = "custom_screen_lightness"
    private const val DEFAULT_CUSTOM_SCREEN_LIGHTNESS = false

    private const val KEY_SCREEN_LIGHTNESS = "screen_lightness"
    private const val DEFAULT_SCREEN_LIGHTNESS = 50

    private const val KEY_GUIDE_GALLERY = "guide_gallery"
    private const val DEFAULT_GUIDE_GALLERY = true

    fun getGuideGallery(): Boolean {
        return SpUtil.getBoolean(AppModule.getContext(), KEY_GUIDE_GALLERY, DEFAULT_GUIDE_GALLERY)
    }

    fun putGuideGallery(value: Boolean) {
        SpUtil.putBoolean(AppModule.getContext(), KEY_GUIDE_GALLERY, value)
    }


    fun getScreenRotation(): Int {
        return SpUtil.getInt(
            AppModule.getContext(),
            KEY_SCREEN_ROTATION,
            DEFAULT_SCREEN_ROTATION
        )
    }

    fun putScreenRotation(value: Int) {
        SpUtil.putInt(AppModule.getContext(), KEY_SCREEN_ROTATION, value)
    }


    @GalleryView.LayoutMode
    fun getReadingDirection(): Int {
        return GalleryView.sanitizeLayoutMode(
            SpUtil.getInt(
                AppModule.getContext(),
                KEY_READING_DIRECTION,
                DEFAULT_READING_DIRECTION
            )
        )
    }

    fun putReadingDirection(value: Int) {
        SpUtil.putInt(
            AppModule.getContext(),
            KEY_READING_DIRECTION,
            value
        )
    }

    @ScaleMode
    fun getPageScaling(): Int {
        return GalleryView.sanitizeScaleMode(
            SpUtil.getInt(
                AppModule.getContext(),
                KEY_PAGE_SCALING,
                DEFAULT_PAGE_SCALING
            )
        )
    }

    fun putPageScaling(value: Int) {
        SpUtil.putInt(AppModule.getContext(), KEY_PAGE_SCALING, value)
    }

    @StartPosition
    fun getStartPosition(): Int {
        return GalleryView.sanitizeStartPosition(
            SpUtil.getInt(
                AppModule.getContext(),
                KEY_START_POSITION,
                DEFAULT_START_POSITION
            )
        )
    }

    fun putStartPosition(value: Int) {
        SpUtil.putInt(
            AppModule.getContext(),
            KEY_START_POSITION,
            value
        )
    }


    fun getKeepScreenOn(): Boolean {
        return SpUtil.getBoolean(AppModule.getContext(), KEY_KEEP_SCREEN_ON, DEFAULT_KEEP_SCREEN_ON)
    }

    fun putKeepScreenOn(value: Boolean) {
        SpUtil.getBoolean(AppModule.getContext(), KEY_KEEP_SCREEN_ON, value)
    }


    fun getShowClock(): Boolean {
        return SpUtil.getBoolean(AppModule.getContext(), KEY_SHOW_CLOCK, DEFAULT_SHOW_CLOCK)
    }

    fun putShowClock(value: Boolean) {
        SpUtil.putBoolean(AppModule.getContext(), KEY_SHOW_CLOCK, value)
    }

    fun getShowProgress(): Boolean {
        return SpUtil.getBoolean(AppModule.getContext(), KEY_SHOW_PROGRESS, DEFAULT_SHOW_PROGRESS)
    }

    fun putShowProgress(value: Boolean) {
        SpUtil.getBoolean(AppModule.getContext(), KEY_SHOW_PROGRESS, value)
    }


    fun getShowBattery(): Boolean {
        return SpUtil.getBoolean(AppModule.getContext(), KEY_SHOW_BATTERY, DEFAULT_SHOW_BATTERY)
    }

    fun putShowBattery(value: Boolean) {
        SpUtil.getBoolean(AppModule.getContext(), KEY_SHOW_BATTERY, value)
    }


    fun getShowPageInterval(): Boolean {
        return SpUtil.getBoolean(
            AppModule.getContext(),
            KEY_SHOW_PAGE_INTERVAL,
            DEFAULT_SHOW_PAGE_INTERVAL
        )
    }

    fun putShowPageInterval(value: Boolean) {
        SpUtil.getBoolean(AppModule.getContext(), KEY_SHOW_PAGE_INTERVAL, value)
    }


    fun getVolumePage(): Boolean {
        return SpUtil.getBoolean(AppModule.getContext(), KEY_VOLUME_PAGE, DEFAULT_VOLUME_PAGE)
    }

    fun putVolumePage(value: Boolean) {
        SpUtil.getBoolean(AppModule.getContext(), KEY_VOLUME_PAGE, value)
    }


    fun getCustomScreenLightness(): Boolean {
        return SpUtil.getBoolean(
            AppModule.getContext(),
            KEY_CUSTOM_SCREEN_LIGHTNESS,
            DEFAULT_CUSTOM_SCREEN_LIGHTNESS
        )
    }

    fun putCustomScreenLightness(value: Boolean) {
        SpUtil.getBoolean(AppModule.getContext(), KEY_CUSTOM_SCREEN_LIGHTNESS, value)
    }


    fun getScreenLightness(): Int {
        return SpUtil.getInt(AppModule.getContext(), KEY_SCREEN_LIGHTNESS, DEFAULT_SCREEN_LIGHTNESS)
    }

    fun putScreenLightness(value: Int) {
        SpUtil.putInt(AppModule.getContext(), KEY_SCREEN_LIGHTNESS, value)
    }

}