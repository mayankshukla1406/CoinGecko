package com.example.relevelandroidproject.domain.model

data class Coin(
    val id : String,
    val name : String,
    val image : String,
    val market_cap : Long,
    val price : Double,
    val price_percent_change : Double,
    val lowPrice : Double,
    val highPrice : Double
)