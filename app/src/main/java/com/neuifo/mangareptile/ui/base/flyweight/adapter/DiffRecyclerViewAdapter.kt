package com.neuifo.mangareptile.ui.base.flyweight.adapter

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.neuifo.mangareptile.ui.base.flyweight.callback.OnItemEventListener
import com.neuifo.mangareptile.ui.base.flyweight.diff.DiffModel
import com.neuifo.mangareptile.ui.base.flyweight.diff.Differential
import com.neuifo.mangareptile.ui.base.flyweight.diff.annotation.*
import java.lang.ClassCastException
import java.lang.RuntimeException
import java.util.concurrent.Executors

/**
 * **描述:** RecyclerView适配器的基类。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/3/29  1:23 PM
 *
 * **版本:** v 1.0.0
 */
abstract class DiffRecyclerViewAdapter<T, VH>(itemEventListener: OnItemEventListener<T>? = null, dataList: MutableList<T> = mutableListOf()) : SuperRecyclerViewAdapter<T, VH>(itemEventListener, dataList) where VH : RecyclerView.ViewHolder, VH : SuperRecyclerViewAdapter.ItemViewHolderInterface<T> {

    companion object {
        /**
         * 当DiffUtil工作完成后所发送的消息标识。
         */
        private const val MSG_DISPATCH_UPDATES = 0x0000_0021
    }

    private val diffUtilCallback = DiffUtilCallback()
    private val tempList = ArrayList<T>()
    /**
     * 用来在主线程处理DiffUtil任务的工具。
     */
    private val refreshHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val diffResult = msg.obj as DiffUtil.DiffResult
            diffResult.dispatchUpdatesTo(this@DiffRecyclerViewAdapter)
            // 通知刷新了之后，要更新副本数据到最新
            tempList.clear()
            tempList.addAll(dataList)
        }
    }
    /**
     * DiffUtil的检索列表数据变化的任务。
     */
    private val refreshRunnable = Runnable {
        refreshHandler.sendMessage(Message.obtain(refreshHandler, MSG_DISPATCH_UPDATES, DiffUtil.calculateDiff(diffUtilCallback)))
    }
    /**
     * 用来执行DiffUtil任务的单线程执行器。
     */
    private val diffExecutor = Executors.newSingleThreadExecutor()

    override fun setData(collection: Collection<T>, refresh: Boolean) {
        if (!refresh) {
            tempList.clear()
            tempList.addAll(collection)
        }
        super.setData(collection, refresh)
    }

    override fun notifyRefresh() {
        if (dataList.isEmpty()) {
            if (tempList.isNotEmpty()) {
                notifyItemRangeRemoved(0, tempList.size)
            }
        } else if (DiffModel::class.java.isAssignableFrom((dataList[0] as Any).javaClass)) {
            diffExecutor.execute(refreshRunnable)
        } else {
            tempList.clear()
            tempList.addAll(dataList)
            super.notifyRefresh()
        }
    }

    /**
     * 当当前Adapter被替换下来以后执行。
     */
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        diffExecutor.shutdown()
        refreshHandler.removeCallbacksAndMessages(null)
    }

    @WorkerThread
    fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when (oldItem) {
            is Differential -> oldItem.areItemsTheSame(newItem as Differential)
            is DiffModel -> areTheSame(oldItem as Any, newItem as Any, true)
            else -> throw RuntimeException("you must implement DiffModel or Differential")
        }
    }

    @WorkerThread
    fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when (oldItem) {
            is Differential -> oldItem.areContentsTheSame(newItem as Differential)
            is DiffModel -> areTheSame(oldItem as Any, newItem as Any, false)
            else -> throw RuntimeException("you must implement DiffModel or Differential")
        }
    }

    @MainThread
    fun getChangePayload(oldItem: T, newItem: T, difference: Bundle) {
        if (oldItem is Differential) {
            oldItem.getChangePayload(newItem as Differential, difference)
        }
    }


    @WorkerThread
    private fun areTheSame(objA: Any, objB: Any, justKey: Boolean): Boolean {
        if (objA.javaClass == objB.javaClass) {
            val aFields = objA.javaClass.declaredFields
            aFields.forEach { field ->
                field.isAccessible = true
                val keyAnnotation = field.getAnnotation(if (justKey) KeyField::class.java else UiField::class.java)
                val valueA = field.get(objA)
                val valueB = field.get(objB)
                if (keyAnnotation != null && valueA != valueB) {
                    return false
                }
                if (!justKey) {
                    val listAnnotation = field.getAnnotation(ListUiField::class.java)
                    if (listAnnotation != null && !areListTheSame(valueA, valueB)) {
                        return false
                    }
                    val arrayAnnotation =  field.getAnnotation(ArrayUiField::class.java)
                    if (arrayAnnotation != null && !areArrayTheSame(valueA, valueB)) {
                        return false
                    }
                }
                val objAnnotation = field.getAnnotation(ObjUiField::class.java)
                if (objAnnotation != null && !areTheSame(valueA, valueB, justKey)) {
                    return false
                }
            }
            return true
        } else {
            return false
        }
    }

    private fun areListTheSame(listA: Any, listB: Any): Boolean {
        return if (listA is List<*> && listB is List<*>) {
            if (listA.size == listB.size) {
                if (listA.isEmpty()) {
                    true
                } else {
                    listA.forEachIndexed { index, any ->
                        if (!when (any) {
                                is Differential -> any.areContentsTheSame(listB[index] as Differential)
                                is DiffModel -> areTheSame(any, listB[index] as Any, false)
                                else -> any != listB[index]
                            }
                        ) return false
                    }
                    true
                }
            } else {
                false
            }
        } else {
            throw ClassCastException("The ${listA.javaClass.simpleName} can't cast to List")
        }
    }

    private fun areArrayTheSame(arrayA: Any, arrayB: Any): Boolean {
        return if (arrayA is Array<*> && arrayB is List<*>) {
            if (arrayA.size == arrayB.size) {
                if (arrayA.isEmpty()) {
                    true
                } else {
                    arrayA.forEachIndexed { index, any ->
                        if (!when (any) {
                                is Differential -> any.areContentsTheSame(arrayB[index] as Differential)
                                is DiffModel -> areTheSame(any, arrayB[index] as Any, false)
                                else -> any != arrayB[index]
                            }
                        ) return false
                    }
                    true
                }
            } else {
                false
            }
        } else {
            throw ClassCastException("The ${arrayA.javaClass.simpleName} can't cast to List")
        }
    }

    override fun addItem(position: Int, itemData: T) {
        tempList.add(position, itemData)
        super.addItem(position, itemData)
    }

    override fun addItem(position: Int, itemData: List<T>) {
        tempList.addAll(position, itemData)
        super.addItem(position, itemData)
    }

    override fun removeItem(position: Int, refresh: Boolean): T? {
        if (refresh) {
            tempList.removeAt(position)
        }
        return super.removeItem(position, refresh)
    }

    override fun removeItem(position: Int, count: Int, refresh: Boolean) {
        if (refresh) {
            for (i: Int in 1..count) {
                tempList.removeAt(position)
            }
        }
        super.removeItem(position, count, refresh)
    }

    override fun removeAll(items: Collection<T>, refresh: Boolean) {
        if (!refresh) {
            tempList.removeAll(items)
        }
        super.removeAll(items, refresh)
    }

    override fun removeItem(item: T): Int {
        val index = dataList.indexOf(item)
        dataList.remove(item)
        tempList.remove(item)
        if (index > -1) {
            notifyItemRemoved(index)
        }
        return index
    }

    override fun moveItem(fromPosition: Int, toPosition: Int) {
        tempList.add(toPosition, tempList.removeAt(fromPosition))
        super.moveItem(fromPosition, toPosition)
    }

    override fun addAll(collection: Collection<T>, refresh: Boolean, index: Int) {
        if (refresh) {
            tempList.addAll(collection)
        }
        super.addAll(collection, refresh, index)
    }

    override fun clear(refresh: Boolean) {
        if (refresh) {
            tempList.clear()
        }
        super.clear(refresh)
    }

    inner class DiffUtilCallback : DiffUtil.Callback() {

        override fun getOldListSize() = tempList.size

        override fun getNewListSize() = dataList.size

        @WorkerThread
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = getOldItem(oldItemPosition)
            val newItem = getNewItem(newItemPosition)
            return if (oldItem == null && newItem == null) {
                true
            } else if (oldItem == null || newItem == null || (oldItem as Any).javaClass!= (newItem as Any).javaClass) {
                false
            } else {
                this@DiffRecyclerViewAdapter.areItemsTheSame(oldItem, newItem)
            }
        }

        @WorkerThread
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = getOldItem(oldItemPosition)
            val newItem = getNewItem(newItemPosition)
            //由于是areItemsTheSame中的代码逻辑是只有两个都为null是才会执行到这个方法所以这里任何一个为null就说明都为null。
            return oldItem == null || newItem == null || this@DiffRecyclerViewAdapter.areContentsTheSame(
                oldItem,
                newItem
            )
        }

        @MainThread
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Bundle? {
            //由areContentsTheSame方法中代码的逻辑可知这里获得的两个对象都不可能为null。
            val oldObject = getOldItem(oldItemPosition)!!
            val newObject = getNewItem(newItemPosition)!!
            val difference = Bundle()
            this@DiffRecyclerViewAdapter.getChangePayload(oldObject, newObject, difference)
            return if (difference.size() == 0) null else difference
        }

        private fun getNewItem(position: Int): T? {
            return if (dataList.size > position && position >= 0) {
                dataList[position]
            } else {
                null
            }
        }

        private fun getOldItem(position: Int): T? {
            return if (tempList.size > position && position >= 0) {
                tempList[position]
            } else {
                null
            }
        }
    }
}
