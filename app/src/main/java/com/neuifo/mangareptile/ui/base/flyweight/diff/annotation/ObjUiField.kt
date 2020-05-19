package com.neuifo.mangareptile.ui.base.flyweight.diff.annotation

/**
 * **描述:** 用来表示一个字段是需要展示在UI中的字段，这个字段如果发生改变就需要刷新UI。
 * 与[UiField]不同的是该注解只能用在不能存储在[android.os.Bundle]中的数据类型上，通常是
 * 没有实现[Serializable] or [android.os.Parcelable]的自定义数据模型上。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/4/9  11:37 AM
 *
 * **版本:** v 1.0.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class ObjUiField