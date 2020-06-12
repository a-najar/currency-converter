package com.geniusforapp.exchange.domain.usecases

class FibonacciNumberUseCase {

    operator fun invoke(number: Int, memo: IntArray): Int {
        if (number == 0 || number == 1) return number
        if (memo[number] == 0) {
            memo[number] = invoke(number - 1, memo) + invoke(number - 2, memo);
        }
        return memo[number]
    }


}