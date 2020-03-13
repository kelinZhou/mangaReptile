package com.neuifo.mangareptile.widget.tablayout.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Gravity;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.neuifo.mangareptile.R;
import com.neuifo.mangareptile.widget.tablayout.CommonToolbarTabTitleView;
import com.neuifo.mangareptile.widget.tablayout.PageNavigatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

/**
 * <strong>描述: </strong> 第三方TabLayout的帮助类。
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/4/19  下午1:34
 * <p><strong>版本: </strong> v 1.0.0
 */

public abstract class ViewPagerTabLayoutHelper {

    @SuppressLint("InflateParams")
    public static MagicIndicator createCommonToolbarTabLayout(@NonNull ViewPager viewPager) {
        MagicIndicator indicator = new MagicIndicator(viewPager.getContext());
        Resources resources = viewPager.getResources();
        int color = resources.getColor(R.color.black_333);
        formatAndAttachToViewPager(indicator, viewPager, color, color, color, LinePagerIndicator.MODE_EXACTLY, false, (context, index) -> {
            CommonToolbarTabTitleView titleView = new CommonToolbarTabTitleView(context);
            titleView.setGravity(Gravity.CENTER);
//            titleView.setPadding(UIUtil.dip2px(context, 30.0), 0, UIUtil.dip2px(context, 30.0), UIUtil.dip2px(context, 15.0) / 2);
            titleView.setSingleLine();
            titleView.setEllipsize(TextUtils.TruncateAt.END);
            titleView.setTextSize(17F);
            return titleView;
        });
        return indicator;
    }

    public static void formatAndAttachToViewPager(@NonNull MagicIndicator indicator, @NonNull ViewPager viewPager) {
        formatAndAttachToViewPager(indicator, viewPager, true);
    }

    public static void formatAndAttachToViewPager(@NonNull MagicIndicator indicator, @NonNull ViewPager viewPager, boolean isFillParent) {
        Context context = viewPager.getContext();
        int selectedColor = ContextCompat.getColor(context, R.color.theme_color);
        formatAndAttachToViewPager(indicator, viewPager, ContextCompat.getColor(context, R.color.black_text_666666), selectedColor, selectedColor, LinePagerIndicator.MODE_EXACTLY, isFillParent, null);
    }

    public static void formatAndAttachToViewPager(@NonNull MagicIndicator indicator, @NonNull ViewPager viewPager, int normalTextColor, int selectedTextColor, int indicatorColor, PageNavigatorAdapter.TitleViewCreator titleViewCreator) {
        formatAndAttachToViewPager(indicator, viewPager, ContextCompat.getColor(viewPager.getContext(), normalTextColor), ContextCompat.getColor(viewPager.getContext(), selectedTextColor), ContextCompat.getColor(viewPager.getContext(), indicatorColor), LinePagerIndicator.MODE_EXACTLY, true, titleViewCreator);
    }

    private static void formatAndAttachToViewPager(@NonNull MagicIndicator indicator, @NonNull ViewPager viewPager, int normalTextColor, int selectedTextColor, int indicatorColor, int indicatorMode, boolean isFillParent, PageNavigatorAdapter.TitleViewCreator titleViewCreator) {
        CommonNavigator commonNavigator = new CommonNavigator(viewPager.getContext());
        commonNavigator.setAdapter(new PageNavigatorAdapter(indicator, viewPager, normalTextColor, selectedTextColor, indicatorColor, indicatorMode, isFillParent, titleViewCreator));
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewPager);
    }

}
