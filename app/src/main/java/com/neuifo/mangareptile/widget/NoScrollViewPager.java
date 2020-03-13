package com.neuifo.mangareptile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * Created by neuifo on 2017/5/19.
 */

public class NoScrollViewPager extends ViewPager {

    private boolean noScroll = true;
    /**
     * 当前的Scroller对象。
     */
    private MyScroller mScroller;

    public NoScrollViewPager(Context context) {
        this(context, null);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        replaceScroller(this, new MyScroller(context, new MyInterpolator()));
    }

    private void replaceScroller(@NonNull ViewPager viewPager, Scroller scroller) {
        try {
            Field scrollerField = getField(ViewPager.class);
            scrollerField.set(viewPager, scroller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    static Field getField(Class cls) {
        Field positionField = null;
        try {
            positionField = cls.getDeclaredField("mScroller");
            positionField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return positionField;
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    /**
     * Banner的页面滚动控制器。
     */
    private class MyScroller extends Scroller {

        /**
         * 用来记录当前的基差。
         */
        private float mCardinal = 1;

        private MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        /**
         * 设置页面滚动的基差。
         *
         * @param cardinal 基差值。
         */
        private void setCardinal(float cardinal) {
            mCardinal = cardinal;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int multiple) {
            //根据设置的减速倍数和剩余需滚动的页面的基差重新计算出一个时长。
            int duration = (int) (multiple * 4 * mCardinal);
            super.startScroll(startX, startY, dx, dy, duration);
        }
    }

    private class MyInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    }
}
