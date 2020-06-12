package com.geniusforapp.exchange.domain.usecases

import org.junit.Assert.*
import org.junit.Test

class FibonacciNumberUseCaseTest {

    @Test
    fun `Given Number 9 when find fib of 9 then return 34 `() {
        val number = 9
        val memo = IntArray(number + 1)
        assertEquals(FibonacciNumberUseCase()(number, memo), 34)
    }


    @Test
    fun `Given Number 0 when find fib of 9th then return 0 `() {
        val number = 0
        val memo = IntArray(number + 1)
        assertEquals(FibonacciNumberUseCase()(number, memo), 0)
    }

    @Test
    fun `Given Number 1 when find fib of 9th then return 1 `() {
        val number = 1
        val memo = IntArray(number + 1)
        assertEquals(FibonacciNumberUseCase()(number, memo), 1)
    }
}