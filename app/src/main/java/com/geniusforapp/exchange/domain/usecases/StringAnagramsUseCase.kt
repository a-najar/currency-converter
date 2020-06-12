package com.geniusforapp.exchange.domain.usecases

class StringAnagramsUseCase {

    operator fun invoke(firstText: String, secondString: String): Boolean {
        if (firstText.length != secondString.length) return false
        val firstTextArray = firstText.toCharArray()
        val secondTextArray = secondString.toCharArray()
        firstTextArray.sort()
        secondTextArray.sort()
        for (i in firstTextArray.indices) {
            if (firstTextArray[i] != secondTextArray[i]) {
                return false
            }
        }
        return true

    }
}