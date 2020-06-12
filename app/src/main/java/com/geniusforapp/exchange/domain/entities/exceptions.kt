package com.geniusforapp.exchange.domain.entities

import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object FailedToRetrieveDataException : Throwable()
open class NetworkException(error: Throwable) : RuntimeException(error)

class NoNetworkException(error: Throwable) : NetworkException(error)

class ServerUnreachableException(error: Throwable) : NetworkException(error)

class HttpCallFailureException(error: Throwable) : NetworkException(error)

fun <T> Single<T>.mapNetworkErrors(): Single<T> =
    this.onErrorResumeNext { error ->
        when (error) {
            is SocketTimeoutException -> Single.error(NoNetworkException(error))
            is UnknownHostException -> Single.error(ServerUnreachableException(error))
            is HttpException -> Single.error(HttpCallFailureException(error))
            else -> Single.error(error)
        }
    }