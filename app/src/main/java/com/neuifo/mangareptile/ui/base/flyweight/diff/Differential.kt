package com.neuifo.mangareptile.ui.base.flyweight.diff

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.lieluobo.candidate.ui.base.flyweight.diff.DiffModel

/**
 * **描述:** 用来表示一个数据模型是可以被[androidx.recyclerview.widget.DiffUtil]所使用的。实现该接口后就只能通过重写接口中的方法来实现字段的对比。
 *
 * @see Differential
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/1/21  1:43 PM
 *
 * **版本:** v 1.0.0
 */
interface Differential : DiffModel {

    /**
     * 判断两个数据模型是否为同一个数据模型。如果指定模型有唯一标识应当以唯一标识去判断。这里的默认实现是通过 {@link #equals(Object)} 方法去判断，
     * 你可以通过重写 [equals(Object)] 方法进行处理，也可以重写该方法进行处理。
     *
     * 如果你没有重写该方法则最好重写 [equals(Object)] 方法进行唯一标识字段的比较，否则有可能会造成不必要刷新的item刷新。
     *
     * @param newItem 新的数据模型。
     *
     * @return 如果相同则应当返回 ```true```,否则应当返回 ```false```。
     */
    @WorkerThread
    fun areItemsTheSame(newItem: Differential): Boolean

    /**
     * 判断两个模型的内容是否相同。
     *
     * 当 [areItemsTheSame(Object, Object)] 方法返回 ```true``` 时，此方法才会被调用，这是因为如果两个对象
     * 的基本特征都是不同的或，就没有进行进一步比较的必要了。
     *
     * 你不必将模型中的所有字段进行比较，只需要将需要展示到UI上的字段进行比较就可以了，你也可以将这个比较放到
     * [equals(Object)] 方法中去做。
     *
     * @param newItem 新的数据模型。
     *
     * @return 如果相同则返回 ```true```, 否则应当返回 ```false```。
     *
     * @see [areItemsTheSame(D, D)]
     */
    @WorkerThread
    fun areContentsTheSame(newItem: Differential): Boolean

    /**
     * 用来获取两个对象不同的部分。
     *
     * 你并不需要将模型中的所有字段进行比较，只需要比较需要展示在界面上的字段就可以了。
     *
     * @param newObject 新的数据模型。
     *
     * @param difference      比较两个对象不同的部分，将两个对象不同的部分以键值对的形式存入该参数中。这个参数你会在
     *                    [onBindPartData(int, Object, Bundle)] 方法中获得。
     */
    @MainThread
    fun getChangePayload(newObject: DiffModel, difference: Bundle)
}