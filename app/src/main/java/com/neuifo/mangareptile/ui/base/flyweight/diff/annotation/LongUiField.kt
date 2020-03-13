package com.lieluobo.candidate.ui.base.flyweight.diff.annotation

import java.lang.annotation.Inherited

/**
 * **描述:** 用来表示一个字段是需要展示在UI中的字段，这个字段如果发生改变就需要刷新UI。
 * 该注解只能用在[Long]类型的数据类型上。
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
annotation class LongUiField(val key: String = "")