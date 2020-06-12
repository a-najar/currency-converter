package com.geniusforapp.exchange.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.NumberFormat
import java.util.*


data class Rates(
    @SerializedName("base")
    val base: String = "",
    @SerializedName("date")
    val date: String = "",
    @SerializedName("rates")
    val rates: Map<String, Float> = mapOf(),
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("timestamp")
    val timestamp: Int = 0
)

@Parcelize
data class Rate(
    val name: String = "",
    val price: Float = 0f,
    val base: String = "",
    val baseRate: Float = 1f
) : Parcelable {

}

fun Float.formatCurrency(code: String): String {
    return NumberFormat.getCurrencyInstance()
        .apply { currency = Currency.getInstance(code) }
        .format(this)
}