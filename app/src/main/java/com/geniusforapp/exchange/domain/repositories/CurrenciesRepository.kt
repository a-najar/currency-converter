package com.geniusforapp.exchange.domain.repositories

import com.geniusforapp.exchange.domain.entities.Rates
import com.geniusforapp.exchange.domain.gateways.FixerGateway
import io.reactivex.rxjava3.core.Single


@Suppress("FunctionName")
fun CurrenciesRepository(): CurrenciesRepository = CurrenciesRepositoryImpl()

interface CurrenciesRepository {
    fun getRates(): Single<Rates>
}

class CurrenciesRepositoryImpl(private val fixerGateway: FixerGateway = FixerGateway()) :
    CurrenciesRepository {
    override fun getRates(): Single<Rates> {
        return fixerGateway.getLatestCurrenciesRates()
    }
}