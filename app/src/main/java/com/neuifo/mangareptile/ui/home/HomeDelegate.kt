package com.neuifo.mangareptile.ui.home

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.FragmentManager
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.delegate.BaseViewDelegate
import com.neuifo.mangareptile.ui.base.flyweight.adapter.CommonFragmentStatePagerAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_home_root.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
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

    fun init(fragmentManager: FragmentManager) {
        navigationAdapter = CommonFragmentStatePagerAdapter(fragmentManager)
        navigationAdapter?.setSaveState(false)
        navigationAdapter?.addPager("实时更新", UpdateComicListFragment::class.java)
        navigationAdapter?.addPager("我的订阅", TestFragment::class.java)
        viewpager.adapter = navigationAdapter
        viewpager.setNoScroll(false)

        commonNavigator7 = CommonNavigator(viewpager.context)
        commonNavigator7?.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return 2
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorFlipPagerTitleView(context, index)
                simplePagerTitleView.text = navigationAdapter!!.getPageTitle(index)
                //simplePagerTitleView.width = ViewUtils.getScreenWidth(context as Activity) / 2
                //simplePagerTitleView.height = ViewUtils.sp2px(context, 60f)
                simplePagerTitleView.normalColor = Color.parseColor("#FFFFFF")
                simplePagerTitleView.selectedColor = Color.parseColor("#00574B")
                simplePagerTitleView.setOnClickListener { viewpager.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.setColors(Color.parseColor("#00574B"))
                indicator.roundRadius = 10f
                indicator.lineWidth = 60f
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                return indicator
            }
        }
        magic_indicator.navigator = commonNavigator7
        ViewPagerHelper.bind(magic_indicator, viewpager)
    }


    interface HomeDelegateCallback : BaseViewDelegate.BaseViewDelegateCallback

    override val rootLayoutId: Int
        get() = R.layout.fragment_home_root


    inner class ColorFlipPagerTitleView(context: Context, var index: Int) :
        SimplePagerTitleView(context) {

        init {
            gravity = Gravity.CENTER
            val padding = UIUtil.dip2px(context, 10.0)
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