package com.atilsamancioglu.solidstock.data.remote

import com.atilsamancioglu.solidstock.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockAPI {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(@Query("apikey") apiKey: String = API_KEY) : ResponseBody

    companion object {
        const val API_KEY = "DCKKXXRUW47F65MS"
        const val BASE_URL = "https://alphavantage.co"
    }

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey : String = API_KEY
    ) : ResponseBody


    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): CompanyInfoDto

}