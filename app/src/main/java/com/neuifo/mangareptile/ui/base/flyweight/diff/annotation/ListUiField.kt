package com.lieluobo.candidate.ui.base.flyweight.diff.annotation

import java.lang.annotation.Inherited

/**
 * **描述:** 用来表示一个字段是[List]且该[List]中的任何一个元素发生了变化都需要刷新UI。
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
annotation class ListUiField