package com.lieluobo.candidate.ui.base.flyweight.diff.annotation

import java.lang.annotation.Inherited

/**
 * **描述:** 用来表示一个字段是一个模型的主要特征或主要特征之一，这个字段如果发生改变就需要刷新UI。
 * 在比较这个字段是否相同时是通过[equals]方法进行比较的。所有用改注解注解的字段必须能够构成一个对象的唯一特征。
 * 通常应该注解在ID字段上，因为通常情况下ID这一个字段就能够构成一个对象的唯一特征。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/4/9  11:35 AM
 *
 * **版本:** v 1.0.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
@Inherited
annotation class KeyField