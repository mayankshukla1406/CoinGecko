package com.example.relevelandroidproject.presentation.CoinList

import com.example.relevelandroidproject.domain.model.Coin

data class CoinListState(
    val isLoading : Boolean = false,
    val coinsList : List<Coin> = emptyList(),
    val error : String = ""
)
