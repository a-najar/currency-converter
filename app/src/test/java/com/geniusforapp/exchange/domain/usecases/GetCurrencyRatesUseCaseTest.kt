package com.geniusforapp.exchange.domain.usecases

import com.geniusforapp.exchange.domain.entities.FailedToRetrieveDataException
import com.geniusforapp.exchange.domain.entities.Rate
import com.geniusforapp.exchange.domain.entities.Rates
import com.geniusforapp.exchange.domain.repositories.CurrenciesRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import java.net.SocketTimeoutException

class GetCurrencyRatesUseCaseTest {


    @Test
    fun `Given Rates with success result the return list of Rate`() {
        val repository: CurrenciesRepository =
            mock { on { getRates() } doReturn Single.just(Rates(success = true, rates = mapOf())) }
        val rates = listOf<Rate>()
        GetCurrencyRatesUseCase(repository)()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertResult(rates)
    }

    @Test
    fun `Given Rates with fail result the return FailedToRetrieveDataException`() {
        val repository: CurrenciesRepository =
            mock { on { getRates() } doReturn Single.just(Rates(success = false, rates = mapOf())) }
        GetCurrencyRatesUseCase(repository)()
            .test()
            .assertError(FailedToRetrieveDataException)
    }


    @Test
    fun `Given Rates with fail to response the return SocketTimeoutException`() {
        val exception = SocketTimeoutException()
        val repository: CurrenciesRepository =
            mock { on { getRates() } doReturn Single.error(exception) }
        GetCurrencyRatesUseCase(repository)()
            .test()
            .assertNotComplete()
    }

}