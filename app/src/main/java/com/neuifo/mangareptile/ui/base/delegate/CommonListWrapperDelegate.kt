package com.neuifo.mangareptile.ui.base.delegate

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.neuifo.mangareptile.ui.base.flyweight.adapter.CommonRecyclerViewAdapter
import com.neuifo.mangareptile.ui.base.flyweight.adapter.SuperRecyclerViewAdapter
import com.lieluobo.candidate.ui.base.flyweight.callback.ItemHolderGenerator
import com.lieluobo.candidate.ui.base.flyweight.callback.OnItemEventListener
import com.neuifo.mangareptile.ui.base.flyweight.viewholder.AbsItemViewHolder
import com.lieluobo.candidate.ui.base.presenter.ViewPresenter
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.mangareptile.AppLayerErrorCatcher
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.flyweight.decoration.DecorationStyles
import com.neuifo.widgetlibs.statelayout.StatePage

import java.util.ArrayList
import java.util.Locale

/**
 * **描述:** 如果你的页面是一个列表页面的话你就应该继承该类。这样可以省去你很多的模板代码。
 *
 *
 * **创建人:** kelin
 * **创建时间:** 2019/3/29  1:33 PM
 * **版本:** v 1.0.0
 */
abstract class CommonListWrapperDelegate<VC : CommonListWrapperDelegate.CommonListDelegateCallback<I>, I, D> :
    CommonViewDelegate<VC, D>(), CommonRecyclerViewAdapter.ItemTypeTranslator<I>,
    ItemHolderGenerator<AbsItemViewHolder<I>, I> {

    private var mLayoutManager: RecyclerView.LayoutManager? = null

    private var mMovingPosition = -1
    private var mIsMoving = false

    private val mOnScrollListener = object : RecyclerView.OnScrollListener() {
        var curState = RecyclerView.SCROLL_STATE_IDLE
        override fun onScrollStateChanged(view: RecyclerView, newState: Int) {
            super.onScrollStateChanged(view, newState)
            curState = newState
            this@CommonListWrapperDelegate.onRecyclerViewScrollStateChanged(view, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            this@CommonListWrapperDelegate.onRecyclerViewScrolled(recyclerView, dx, dy, curState)

            //在这里进行第二次滚动（最后的100米！）
            if (mIsMoving) {
                mIsMoving = false
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                val n = mMovingPosition - (mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (0 <= n && n < recyclerView.childCount) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    val top = recyclerView.getChildAt(n).top
                    //最后的移动
                    recyclerView.scrollBy(0, top)
                }
            }
        }
    }

    override val pageStateFlags: Int
        get() = StatePage.HAVE_EMPTY_STATE or super.pageStateFlags

    override val rootLayoutId: Int
        get() = R.layout.common_list

    protected open val swipeLayoutId: Int
        get() = R.id.swipe_refresh_layout

    private val swipeRefreshLayout: SwipeRefreshLayout?
        get() = getView(swipeLayoutId)

    protected val recyclerView: RecyclerView by lazy {
        getView<RecyclerView>(R.id.list_view) ?: throw NullPointerException("RecyclerView is null object!")
    }

    @Suppress("UNCHECKED_CAST")
    protected val adapter: SuperRecyclerViewAdapter<I, AbsItemViewHolder<I>>
        get() = recyclerView.adapter as SuperRecyclerViewAdapter<I, AbsItemViewHolder<I>>

    protected val visiblePositions: IntArray
        get() {
            return if (mLayoutManager is LinearLayoutManager) {
                val first = (mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val last = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                intArrayOf(first, last)
            } else {
                val lastChildView = mLayoutManager!!.getChildAt(mLayoutManager!!.childCount - 1)
                val firstChildView = mLayoutManager!!.getChildAt(0)

                val firstPosition = if (firstChildView == null) 0 else mLayoutManager!!.getPosition(firstChildView)
                val lastPosition = if (lastChildView == null) 0 else mLayoutManager!!.getPosition(lastChildView)
                intArrayOf(firstPosition, lastPosition)
            }
        }

    protected val visibleItems: List<I>
        get() {
            val pos = visiblePositions
            val start = pos[0]
            val end = pos[1]

            if (start < 0 || end < 0) {
                return emptyList()
            }

            val adapter = adapter
            val len = end - start
            val count = adapter.getItemCount()
            if (start > count - 1 || end > count - 1) {
                LogHelper.system.e(
                    "==========getVisibleItems==========",
                    String.format(
                        Locale.CHINA,
                        "Visible item error: start %d, end %s, but total count is %d",
                        start,
                        end,
                        count
                    )
                )
                return emptyList()
            }

            val r = ArrayList<I>()
            for (i in 0 until len + 1) {
                r.add(adapter.getItem(start + i))
            }
            return r

        }

    interface CommonListDelegateCallback<D> : CommonViewDelegateCallback {

        fun onListItemClick(position: Int, item: D)

        fun onListItemLongClick(position: Int, item: D)

        fun onLoadMore(retry: Boolean)

        fun onListItemChildClick(position: Int, v: View, item: D)

        fun isLoadMoreEnable(): Boolean
    }


    override fun bindView(viewPresenter: ViewPresenter<VC>) {
        super.bindView(viewPresenter)
        val refreshView = swipeRefreshLayout
        if (refreshView != null) {
            refreshView.setColorSchemeResources(R.color.theme_color)
            refreshView.setOnRefreshListener {
                viewCallback.onRefresh()
            }
        }
    }

    override fun presentView(viewPresenter: ViewPresenter<VC>, savedInstanceState: Bundle?) {
        super.presentView(viewPresenter, savedInstanceState)
        setupRecyclerView(recyclerView)
    }

    @CallSuper
    override fun setInitialData(data: D) {
        val listData = extractInitialDataList(data)
        if (listData.isEmpty()) {
            showEmptyView()
        } else {
            adapter.setData(listData)
        }
    }

    @CallSuper
    fun addMoreData(data: D) {
        adapter.addAll(extractInitialDataList(data), true)
    }

    fun addHeadData(data: List<I>) {
        adapter.addItem(0, data.filter { item ->
            !adapter.dataList.contains(item).apply {
                if (this) {
                    adapter.notifyItem(item)
                }
            }
        })
    }

    protected abstract fun extractInitialDataList(initialData: D): Collection<I>


    private fun setupRecyclerView(recyclerView: RecyclerView?) {
        recyclerView!!.setHasFixedSize(true)
        // 设置Item增加、移除动画
        val animator = DefaultItemAnimator()
        animator.addDuration = 200
        animator.changeDuration = 100
        animator.removeDuration = 200
        animator.moveDuration = 200
        recyclerView.itemAnimator = animator
        // 设置布局管理器（LinearLayoutManager 现行管理器，支持横向、纵向。
        // GridLayoutManager 网格布局管理器
        // StaggeredGridLayoutManager 瀑布就式布局管理器）
        mLayoutManager = onCreateLayoutManager()
        recyclerView.layoutManager = mLayoutManager
        val option = decorationOption
        if (option != null) {
            DecorationStyles.setDecorationStyle(recyclerView, option)
        }
        recyclerView.addOnScrollListener(mOnScrollListener)

        // 添加分割线
        //recyclerView.addItemDecoration(new RecyclerViewItemDecoration(context, RecyclerViewItemDecoration.HORIZONTAL_LIST));

        if (recyclerView.adapter == null) {
            val adapter = onCreateAdapter()
            recyclerView.adapter = adapter
        }
    }

    protected open val decorationOption: DecorationStyles.Options?
        get() = null

    protected open fun onCreateLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(context, VERTICAL, false) {
            override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: IndexOutOfBoundsException) {
                    AppLayerErrorCatcher.throwException(e)
                }
            }
        }
    }

    protected open fun onRecyclerViewScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

    }

    protected open fun onRecyclerViewScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int, curState: Int) {

    }

    /**
     * 创建adapter
     */
    protected open fun onCreateAdapter(): SuperRecyclerViewAdapter<I, AbsItemViewHolder<I>> {
        return CommonRecyclerViewAdapter(this, this, CommonOnItemEventListenerImpl()).apply {
            if (viewCallback.isLoadMoreEnable()) {
                setLoadMoreView(R.layout.layout_load_more, R.layout.layout_load_more_failed, 0, 2) {
                    viewCallback.onLoadMore(it)
                }
            }
        }
    }

    fun reSetRefresh() {
        swipeRefreshLayout?.isRefreshing = false
    }

    /**
     * 当条目被点击时调用。
     * @param position 被点击的条目的索引。
     * @param item 当前条目的数据模型。
     * @return 如果返回true则表示自己消费，返回false则会继续想ViewPresenter中传递。
     */
    protected open fun onItemClick(position: Int, item: I): Boolean {
        return false
    }

    /**
     * 当条目被长按时调用。
     * @param position 被点击的条目的索引。
     * @param item 当前条目的数据模型。
     * @return 如果返回true则表示自己消费，返回false则会继续想ViewPresenter中传递。
     */
    protected open fun onItemLongClick(position: Int, item: I): Boolean {
        return false
    }

    /**
     * 当条目中的子View被点击时调用。
     * @param position 被点击的条目的索引。
     * @param v 被点击的子View。
     * @param item 当前条目的数据模型。
     * @return 如果返回true则表示自己消费，返回false则会继续想ViewPresenter中传递。
     */
    protected open fun onItemChildClick(position: Int, v: View, item: I): Boolean {
        return false
    }

    open fun showLoadMoreError(err: String) {}

    open fun moveToPosition(position: Int) {
        mMovingPosition = position
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            // 先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
            val firstItem = layoutManager.findFirstVisibleItemPosition()
            val lastItem = layoutManager.findLastVisibleItemPosition()

            // 然后区分情况
            when {
                position <= firstItem -> // 当要置顶的项在当前显示的第一个项的前面时
                    recyclerView.scrollToPosition(position)
                position <= lastItem -> {
                    // 当要置顶的项已经在屏幕上显示时
                    val top = recyclerView.getChildAt(position - firstItem).top
                    recyclerView.scrollBy(0, top)
                }
                else -> {
                    // 当要置顶的项在当前显示的最后一项的后面时
                    recyclerView.scrollToPosition(position)
                    // 这里这个变量是用在RecyclerView滚动监听里面的
                    mIsMoving = true
                }
            }
        }
    }

    fun setNoMoreData(noMoreData: Boolean) {
        adapter.setNoMoreData(noMoreData)
    }

    fun isNoMoreData(): Boolean {
        return adapter.isNoMoreData()
    }

    fun setLoadMoreFinish() {
        adapter.setLoadMoreFinish()
    }

    fun getData(posi: Int): I {
        return adapter.getItem(posi)
    }

    fun getLastData(): I? {
        return adapter.dataList.lastOrNull()
    }

    fun setLoadMoreFailed() {
        adapter.setLoadMoreFailed()
    }

    fun notifyItem(position: Int) {
        adapter.notifyItemChanged(position)
    }

    fun addItem(data: I) {
        adapter.addItem(data)
    }

    fun notifyItem(data: I) {
        adapter.notifyItem(data)
    }

    fun getItemPosi(data: I): Int {
        return adapter.getItemPosi(data)
    }

    internal inner class CommonOnItemEventListenerImpl : OnItemEventListener<I> {

        override fun onItemClick(position: Int, item: I) {
            if (!this@CommonListWrapperDelegate.onItemClick(position, item)) {
                viewCallback.onListItemClick(position, item)
            }
        }

        override fun onItemLongClick(position: Int, item: I) {
            if (!this@CommonListWrapperDelegate.onItemLongClick(position, item)) {
                viewCallback.onListItemLongClick(position, item)
            }
        }

        override fun onItemChildClick(v: View, position: Int, item: I) {
            if (!this@CommonListWrapperDelegate.onItemChildClick(position, v, item)) {
                viewCallback.onListItemChildClick(position, v, item)
            }
        }
    }
}
