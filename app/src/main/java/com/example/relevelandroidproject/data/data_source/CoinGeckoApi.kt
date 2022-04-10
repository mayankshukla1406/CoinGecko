package com.example.relevelandroidproject.data.data_source.dto

import com.example.relevelandroidproject.data.data_source.dto.CoinDetailDTO.CoinDetailDto
import com.example.relevelandroidproject.data.data_source.dto.CoinListDTO.CoinListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&sparkline=false")
    suspend fun getAllCoins(@Query("page")page:String):CoinListDto

    @GET("/api/v3/coins/{id}")
    suspend fun getCoinById(@Path("id")id:String): CoinDetailDto
}