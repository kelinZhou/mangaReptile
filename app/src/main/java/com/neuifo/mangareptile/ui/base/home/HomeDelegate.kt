package com.neuifo.mangareptile.ui.base.home

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.fragment.app.FragmentManager
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.delegate.BaseViewDelegate
import com.neuifo.mangareptile.ui.base.flyweight.adapter.CommonFragmentStatePagerAdapter
import kotlinx.android.extensions.LayoutContainer
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import kotlinx.android.synthetic.main.fragment_home_root.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

class HomeDelegate : BaseViewDelegate<HomeDelegate.HomeDelegateCallback>(), LayoutContainer {

    fun isEmpty(): Boolean {
        return navigationAdapter == null
    }

    private var navigationAdapter: CommonFragmentStatePagerAdapter? = null
    private var commonNavigator7: CommonNavigator? = null
    private var showNumberHint = false

    fun init(fragmentManager: FragmentManager) {
        navigationAdapter = CommonFragmentStatePagerAdapter(fragmentManager)
        navigationAdapter?.addPager("我的订阅", TestFragment::class.java)
        navigationAdapter?.addPager("实时更新", TestFragment::class.java)

        val commonNavigator7 = CommonNavigator(context)
        commonNavigator7.scrollPivotX = 0.65f
        commonNavigator7.adapter = object : CommonNavigatorAdapter() {

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorFlipPagerTitleView(context, index)
                simplePagerTitleView.text = navigationAdapter!!.getPageTitle(index)
                //simplePagerTitleView.width = ViewUtils.getScreenWidth(context as Activity) / 2
                //simplePagerTitleView.height = ViewUtils.sp2px(context, 60f)
                simplePagerTitleView.normalColor = Color.parseColor("#FFFFFF")
                simplePagerTitleView.selectedColor = Color.parseColor("#FFFFFF")
                simplePagerTitleView.textSize = 17f
                simplePagerTitleView.setOnClickListener { viewpager.currentItem = index }

                return simplePagerTitleView
            }

            override fun getCount(): Int = 2

            override fun getIndicator(context: Context?): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.lineWidth = 40f
                indicator.roundRadius = 10f
                indicator.startInterpolator = AccelerateInterpolator() as Interpolator
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(Color.parseColor("#FFFFFF"))
                return indicator
            }

        }

        magic_indicator!!.navigator = commonNavigator7
        viewpager.setNoScroll(false)
        viewpager.offscreenPageLimit = 2

        viewpager.adapter = navigationAdapter

        ViewPagerHelper.bind(magic_indicator, viewpager)

    }


    interface HomeDelegateCallback : BaseViewDelegate.BaseViewDelegateCallback

    override val rootLayoutId: Int
        get() = R.layout.fragment_home_root


    inner class ColorFlipPagerTitleView(context: Context, var index: Int) :
        SimplePagerTitleView(context) {

        init {
            gravity = Gravity.CENTER
            val padding = UIUtil.dip2px(context, 30.0)
            setPadding(padding, 0, padding, 0)
            setSingleLine()
            ellipsize = TextUtils.TruncateAt.END
        }

        override fun onSelected(index: Int, totalCount: Int) {
            super.onSelected(index, totalCount)
            paint.isFakeBoldText = true
            setTextColor(mSelectedColor)
            if (mSelectedColor == mNormalColor) {
                invalidate()
            }
        }

        override fun onDeselected(index: Int, totalCount: Int) {
            super.onDeselected(index, totalCount)
            paint.isFakeBoldText = false
            setTextColor(mNormalColor)
            if (mSelectedColor == mNormalColor) {
                invalidate()
            }
        }
    }
}