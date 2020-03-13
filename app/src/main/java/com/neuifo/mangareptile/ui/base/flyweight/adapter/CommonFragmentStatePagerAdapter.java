package com.neuifo.mangareptile.ui.base.flyweight.adapter;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.neuifo.mangareptile.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述 FragmentPager {@link androidx.viewpager.widget.ViewPager ViewPager}的适配器。
 * 创建人 kelin
 * 创建时间 16/9/18  上午11:44
 * 包名 com.chengshi.iot.adapter
 */
public class CommonFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    /**
     * 当前{@link androidx.viewpager.widget.ViewPager ViewPager}的所有页面的{@link Class}对象。
     */
    private List<Class<? extends BaseFragment>> mClsList;
    /**
     * 当前{@link androidx.viewpager.widget.ViewPager ViewPager}的所有页面的名字。
     */
    private List<String> mNameList;
    /**
     * 当前{@link androidx.viewpager.widget.ViewPager ViewPager}的所有页面的{@link BaseFragment}对象。
     */
    private SparseArray<BaseFragment> mFragmentMap;

    private boolean mSaveState = true;
    private Fragment fragment;

    /**
     * 构造方法。
     *
     * @param fm 需要一个{@link FragmentManager}对象。
     */
    public CommonFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        mClsList = new ArrayList<>();
        mNameList = new ArrayList<>();
        mFragmentMap = new SparseArray<>();
    }

    @Override
    public Parcelable saveState() {
        return mSaveState ? super.saveState() : null;
    }

    public void setSaveState(boolean saveState) {
        mSaveState = saveState;
    }

    /**
     * 添加页面。
     *
     * @param name 要添加的页面的名字。这个名字将会显示在
     *             {@linkplain TabLayout 页签}中。
     * @param cls  要添加的页面的{@link Class}对象。
     */
    public void addPager(String name, Class<? extends BaseFragment> cls) {
        //防止添加重复的页面。
        if (mNameList.contains(name)) {
            throw new RuntimeException("the name: “" + name + "” is contains!");
        }
        //健壮性判断，防止添加失败。
        if (mNameList.add(name)) {
            if (!mClsList.add(cls)) {
                mNameList.remove(name);
            }
        }
    }

    /**
     * 添加页面。
     *
     * @param name     要添加的页面的名字。这个名字将会显示在
     *                 {@linkplain TabLayout 页签}中,如果你的页面中有的话。
     * @param fragment 要添加的页面的{@link BaseFragment}对象。
     */
    public void addPager(String name, BaseFragment fragment) {
        //防止添加重复的页面。
        if (mNameList.contains(name)) {
            new RuntimeException("the name: “" + name + "” is contains!").printStackTrace();
        }
        //健壮性判断，防止添加失败。
        if (mNameList.add(name)) {

            if (!mClsList.add(fragment.getClass())) {
                mNameList.remove(name);
            } else {
                if (mFragmentMap == null) {
                    mFragmentMap = new SparseArray<>();
                }
                mFragmentMap.put(mFragmentMap.size(), fragment);
            }
        }
    }

    @Override
    public int getCount() {
        return mClsList == null ? 0 : mClsList.size();
    }


    @Override
    public BaseFragment getItem(int position) {
        // Log.d("CommonFragmentStatePagerAdapter", "getItem: " + position);
        return getInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Log.d("CommonFragmentStatePagerAdapter", "instantiateItem: " + position);
        BaseFragment fragment = (BaseFragment) super.instantiateItem(container, position);
        mFragmentMap.put(position, fragment);
        return fragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

//        Fragment fragment = (Fragment) object;
//        if (fragment != null) {
            // 可能出现fragment == mCurrentPrimaryItem，从而setUserVisibleHint(true)无法执行
//            fragment.setMenuVisibility(true);
//            fragment.setUserVisibleHint(true);
//        }
        // int old = mPrimaryItem == null ? 0 : mPrimaryItem.hashCode();
        // Log.d("CommonFragmentStatePagerAdapter", "setPrimaryItem: " + position + " " + old + "<==>" + object.hashCode());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Log.d("CommonFragmentStatePagerAdapter", "destroyItem: " + position);
        super.destroyItem(container, position, object);
    }

    /**
     * 根据索引获取{@link BaseFragment}。
     *
     * @param position 要获取的Fragment的索引。
     * @return 返回一个 {@link BaseFragment}。
     */
    private BaseFragment getInstance(int position) {
        BaseFragment fragment = null;
        if (mFragmentMap == null) {
            mFragmentMap = new SparseArray<>();
        } else {
            fragment = mFragmentMap.get(position);
            if (fragment != null) {
                return fragment;
            }
        }

        try {
            fragment = mClsList.get(position).newInstance();
            if (fragment != null) {
                mFragmentMap.put(position, fragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }

    /**
     * 获取指定页面的标题。
     * 主要是用来给{@link TabLayout 页签}设置标题。
     *
     * @param position 要获取页面的position。
     * @return 返回当前页面的标题。
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mNameList.get(position);
    }

    /**
     * 清空页面
     */
    public void clear() {
        clear(true);
    }

    /**
     * 清空页面。
     *
     * @param isRefresh 是否在清空之后刷新页面。
     */
    public void clear(boolean isRefresh) {
        if (mNameList != null) {
            mNameList.clear();
        }
        if (mClsList != null) {
            mClsList.clear();
        }
        if (mFragmentMap != null) {
            mFragmentMap.clear();
        }
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

}
