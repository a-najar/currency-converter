package com.geniusforapp.exchange.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geniusforapp.exchange.domain.entities.Rate
import com.geniusforapp.exchange.domain.usecases.ConverterUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ConverterViewModel(
    private val rate: Rate,
    private val converterUseCase: ConverterUseCase,
    private val compositeDisposable: CompositeDisposable,
    io: Scheduler = Schedulers.io(),
    main: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {

    private var amountSubject: BehaviorSubject<Float> = BehaviorSubject.createDefault(1f)
    private var swapSubject: BehaviorSubject<String> = BehaviorSubject.createDefault(rate.base)

    val rateLiveData: LiveData<Rate>
    val convertAmount: LiveData<Float>

    init {
        rateLiveData = MutableLiveData()
        convertAmount = MutableLiveData()

        Observable.just(rate)
            .subscribeOn(io)
            .observeOn(main)
            .repeatWhen { amountSubject }
            .repeatWhen { swapSubject }
            .doOnNext { rateLiveData.postValue(it) }
            .switchMap { calculateRate(amountSubject.value) }
            .subscribe(convertAmount::postValue)
            .also { compositeDisposable.add(it) }
    }

    private fun calculateRate(amount: Float): Observable<Float> {
        val convertedAmount = if (swapSubject.value == rate.base) {
            converterUseCase.convert(amount, rate.price)
        } else {
            converterUseCase.convertBack(amount, rate.price)
        }
        return Observable.just(convertedAmount)
    }

    fun convert(amount: Float) {
        amountSubject.onNext(amount)
    }

    fun swap() {
        swapSubject.onNext(if (swapSubject.value == rate.base) rate.name else rate.base)
    }
}

class ConverterFactory(
    private val rate: Rate,
    private val converterUseCase: ConverterUseCase = ConverterUseCase(),
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConverterViewModel(rate, converterUseCase, compositeDisposable) as T
    }

}