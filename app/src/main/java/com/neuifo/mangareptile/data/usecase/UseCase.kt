package com.neuifo.mangareptile.data.usecase

import com.neuifo.mangareptile.data.core.API
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers

abstract class UseCase<DATA> {

    private var subscription = Observable.empty<DATA>()


    protected abstract fun buildUseCaseObservable(): Observable<DATA>

    fun execute(observer: Observer<DATA>) {
        this.subscription = this.buildUseCaseObservable()
        subscription.subscribeOn(Schedulers.from(API.provideThreadExecutor))
            .observeOn(API.postExecutionThread.scheduler)
            .subscribe(observer)
    }
}
