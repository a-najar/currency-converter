package com.geniusforapp.exchange.ui.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geniusforapp.exchange.domain.entities.Rate
import com.geniusforapp.exchange.domain.usecases.GetCurrencyRatesUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class CurrenciesViewModel(
    getCurrencyRatesUseCase: GetCurrencyRatesUseCase = GetCurrencyRatesUseCase(),
    io: Scheduler = Schedulers.io(),
    main: Scheduler = AndroidSchedulers.mainThread(),
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
) : ViewModel() {
    private val timer: BehaviorSubject<Long> = BehaviorSubject.create()

    val currencyLiveData: LiveData<List<Rate>>
    val errorLiveData: LiveData<Throwable>
    val loaderLiveData: LiveData<Boolean>
    val timerLiveData: LiveData<Long>
    val baseLiveData: LiveData<String>

    init {
        currencyLiveData = MutableLiveData()
        errorLiveData = MutableLiveData()
        loaderLiveData = MutableLiveData()
        timerLiveData = MutableLiveData()
        baseLiveData = MutableLiveData()

        Observable.interval(TIME_INTERVAL, TimeUnit.SECONDS)
            .repeat()
            .subscribeOn(io)
            .subscribe(timer)

        getCurrencyRatesUseCase()
            .toObservable()
            .repeatWhen { timer }
            .subscribeOn(io)
            .observeOn(main)
            .doOnSubscribe { loaderLiveData.postValue(true) }
            .doAfterNext { loaderLiveData.postValue(false) }
            .doAfterNext { startCountDown(timerLiveData) }
            .doOnNext { baseLiveData.postValue(it.first().base) }
            .subscribe(currencyLiveData::postValue, errorLiveData::postValue)
            .also { compositeDisposable.add(it) }


    }

    private fun startCountDown(timerLiveData: MutableLiveData<Long>) {
        Observable.interval(1, TimeUnit.SECONDS)
            .take(TIME_INTERVAL + 1)
            .map { TIME_INTERVAL - it - 1 }
            .filter { it >= 0 }
            .doOnNext { timerLiveData.postValue(it) }
            .subscribe({}, {})
            .also { compositeDisposable.add(it) }
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}

const val TIME_INTERVAL = 60L