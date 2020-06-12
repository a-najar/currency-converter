package com.geniusforapp.exchange.domain.usecases

import org.junit.Assert.*
import org.junit.Test

class ArithmeticUseCaseKtTest {

    @Test
    fun `Given Numbers when we do (add, subtract, multiply, divide) it should return 12`() {
        val findArithmetic = findArithmetic()
        assertEquals(findArithmetic, 12)
    }
}