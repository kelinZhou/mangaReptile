package com.lieluobo.candidate.ui.base.flyweight.diff


/**
 * **描述:** 用来表示一个数据模型是可以被[android.support.v7.util.DiffUtil]所使用的，不过该数据模型中的所有需要对比的字段
 * 只能通过注解的方式进行对比。如果不希望使用注解而是希望自己实现，则要实现[DiffModel]的子类[Differential]。
 *
 * @see KeyField
 * @see UiField
 * @see ObjUiField
 * @see Differential
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/1/21  1:43 PM
 *
 * **版本:** v 1.0.0
 */
interface DiffModel