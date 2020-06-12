package com.geniusforapp.exchange.domain.usecases

import org.junit.Assert.*
import org.junit.Test

class StringAnagramsUseCaseTest {


    @Test
    fun `Given Two strings is anagrams should return true`() {
        val firstText = "Ahmad"
        val secondText = "Ahmad"
        val check = StringAnagramsUseCase()(firstText, secondText)
        assertEquals(check, true)
    }

    @Test
    fun `Given Two strings is not anagrams should return false`() {
        val firstText = "Ahmad"
        val secondText = "Sobhi"
        val check = StringAnagramsUseCase()(firstText, secondText)
        assertEquals(check, false)
    }
}