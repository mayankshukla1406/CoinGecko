package com.example.relevelandroidproject.data.repository

import android.util.Log
import com.example.relevelandroidproject.data.data_source.dto.CoinDetailDTO.CoinDetailDto
import com.example.relevelandroidproject.data.data_source.dto.CoinGeckoApi
import com.example.relevelandroidproject.data.data_source.dto.CoinListDTO.CoinListDto
import com.example.relevelandroidproject.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api : CoinGeckoApi
) : CoinRepository{
    override suspend fun getAllCoins(page:String): CoinListDto {
        return api.getAllCoins(page=page)
    }

    override suspend fun getCoinById(id: String): CoinDetailDto {

        return api.getCoinById(id=id)
    }
}