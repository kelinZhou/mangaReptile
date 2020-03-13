package com.neuifo.mangareptile.data.usecase

import com.neuifo.mangareptile.data.proxy.PageActionParameter
import io.reactivex.Observable

/**
 * **描述:** 有分页功能的UseCase。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/4/4  1:35 PM
 *
 * **版本:** v 1.0.0
 */
class PageApiIdRequestUseCase<ID, DATA>(private val id: ID, private val pages: PageActionParameter.Pages, private val caller: (id: ID, pages: PageActionParameter.Pages) -> Observable<DATA>) : UseCase<DATA>() {
    override fun buildUseCaseObservable(): Observable<DATA> {
        return caller(id, pages)
    }
}