package com.neuifo.mangareptile.widget.tablayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.animation.DecelerateInterpolator;


import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.neuifo.data.util.DeviceUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.jetbrains.annotations.NotNull;

/**
 * <strong>描述: </strong> ViewPager指示器的适配器。
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/6/19  下午6:27
 * <p><strong>版本: </strong> v 1.0.0
 */
public class PageNavigatorAdapter extends CommonNavigatorAdapter implements ViewPager.OnAdapterChangeListener {
    private final ViewPager viewPager;
    private PagerAdapter adapter;
    @ColorInt
    private int normalTextColor;
    @ColorInt
    private int selectedTextColor;
    @ColorInt
    private int indicatorColor;
    private int indicatorMode;
    private boolean isFillParent;
    private TitleViewCreator titleViewCreator;

    public PageNavigatorAdapter(MagicIndicator parent, ViewPager viewPager, int normalTextColor, int selectedTextColor, int indicatorColor, int indicatorMode, boolean isFillParent, TitleViewCreator titleViewCreator) {
        this.normalTextColor = normalTextColor;
        this.selectedTextColor = selectedTextColor;
        this.indicatorColor = indicatorColor;
        this.indicatorMode = indicatorMode;
        this.viewPager = viewPager;
        this.adapter = viewPager.getAdapter();
        this.isFillParent = isFillParent;
        this.titleViewCreator = titleViewCreator;
        if (adapter == null) {
            viewPager.addOnAdapterChangeListener(this);
            listenerDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return adapter.getCount();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        IPagerTitleView titleView = onCreateTitleView(context,index);
        return titleView;
    }

    @NotNull
    private IPagerTitleView onCreateTitleView(Context context, int index) {
        SimplePagerTitleView titleView;
        if (titleViewCreator == null) {
            titleView = new CommonToolbarTabTitleView(context, null);
            titleView.setTextColor(14);
            titleView.setTextSize(13);
        } else {
            titleView =  titleViewCreator.createTitleView(context,index);
        }
        titleView.setNormalColor(normalTextColor);
        titleView.setSelectedColor(selectedTextColor);
        titleView.setText(adapter.getPageTitle(index));
        if (isFillParent) {
            titleView.setWidth(DeviceUtils.INSTANCE.getScreenWidth(context, 1.0) / getCount());
        }
        titleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
        return titleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(indicatorMode);
        indicator.setColors(indicatorColor);
        indicator.setRoundRadius(10);
        indicator.setLineWidth(60);
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        return indicator;
    }

    @Override
    public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
        this.adapter = newAdapter;
        if (newAdapter != null) {
            listenerDataSetChanged();
        }
    }

    private void listenerDataSetChanged() {
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }
        });
    }


    public interface TitleViewCreator {
        SimplePagerTitleView createTitleView(Context context, int index);
    }
}
