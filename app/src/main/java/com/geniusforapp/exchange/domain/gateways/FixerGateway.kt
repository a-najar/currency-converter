package com.geniusforapp.exchange.domain.gateways

import com.geniusforapp.exchange.BuildConfig
import com.geniusforapp.exchange.domain.entities.Rates
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val ACCESS_KEY = "access_key"

interface FixerGateway {
    @GET("latest")
    fun getLatestCurrenciesRates(): Single<Rates>
}

internal val interceptor = Interceptor {
    val request = it.request()
    val url = request.url
        .newBuilder()
        .addQueryParameter(ACCESS_KEY, BuildConfig.TOKEN)
        .build()
    it.proceed(request.newBuilder().url(url).build())
}

internal val okHttp: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

internal val retrofit: Retrofit = Retrofit.Builder()
    .client(okHttp)
    .baseUrl(BuildConfig.FIXER_URL)
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()


@Suppress("FunctionName")
fun FixerGateway(): FixerGateway = retrofit.create(FixerGateway::class.java)