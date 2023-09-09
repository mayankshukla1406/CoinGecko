package com.example.relevelandroidproject.domain.repository

import com.example.relevelandroidproject.data.data_source.dto.CoinDetailDTO.CoinDetailDto
import com.example.relevelandroidproject.data.data_source.dto.CoinListDTO.CoinListDto
import com.example.relevelandroidproject.data.data_source.dto.CoinListDTO.CoinListDtoItem

interface CoinRepository {

    suspend fun getAllCoins(page:String): List<CoinListDtoItem>

    suspend fun getCoinById(id:String): CoinDetailDto

}