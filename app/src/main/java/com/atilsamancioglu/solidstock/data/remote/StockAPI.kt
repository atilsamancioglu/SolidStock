package com.atilsamancioglu.solidstock.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockAPI {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(@Query("apikey") apiKey: String) : ResponseBody

    companion object {
        const val API_KEY = "DCKKXXRUW47F65MS"
        const val BASE_URL = "https://alphavantage.co"
    }

}