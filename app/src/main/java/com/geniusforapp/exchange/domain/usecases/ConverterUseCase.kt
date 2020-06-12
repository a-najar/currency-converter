package com.geniusforapp.exchange.domain.usecases

class ConverterUseCase {

    fun convert(amount: Float, rate: Float): Float {
        return amount * rate
    }

    fun convertBack(amount: Float, rate: Float): Float {
        return amount / rate
    }
}