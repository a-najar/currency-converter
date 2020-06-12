package com.geniusforapp.exchange.domain.usecases

import org.junit.Assert.*
import org.junit.Test

class ConverterUseCaseTest {

    @Test
    fun `Given amount and want to convert it to currency amount`() {
        val converter = ConverterUseCase()
        assertEquals(converter.convert(1f, 4.13f), 4.13f)
    }

    @Test
    fun `Given amount 100 and want to convert it to currency amount`() {
        val converter = ConverterUseCase()
        assertEquals(converter.convert(100f, 4.13f), 413f)
    }

    @Test
    fun `Given amount 413 and want to convert back it to currency amount 100`() {
        val converter = ConverterUseCase()
        assertEquals(converter.convertBack(413f, 4.13f), 100f)
    }

}