package com.geniusforapp.exchange.domain.usecases

import com.geniusforapp.exchange.domain.entities.FailedToRetrieveDataException
import com.geniusforapp.exchange.domain.entities.Rate
import com.geniusforapp.exchange.domain.entities.Rates
import com.geniusforapp.exchange.domain.entities.mapNetworkErrors
import com.geniusforapp.exchange.domain.repositories.CurrenciesRepository
import io.reactivex.rxjava3.core.Single

class GetCurrencyRatesUseCase(private val repository: CurrenciesRepository = CurrenciesRepository()) :
        () -> Single<List<Rate>> {

    override fun invoke(): Single<List<Rate>> {
        return repository.getRates()
            .mapNetworkErrors()
            .validateData()
            .map { it.base to it.rates }
            .map { it.second.map { rate -> Rate(rate.key, rate.value, it.first) } }
    }
}

fun Single<Rates>.validateData(): Single<Rates> {
    return compose {
        filter { it.success }
            .switchIfEmpty(Single.error(FailedToRetrieveDataException))
    }
}