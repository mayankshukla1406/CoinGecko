package com.example.relevelandroidproject.data.data_source.dto.CoinDetailDTO

import com.google.gson.annotations.SerializedName

data class Localization(
    val ar: String,
    val bg: String,
    val cs: String,
    val da: String,
    val de: String,
    val el: String,
    val en: String,
    val es: String,
    val fi: String,
    val fr: String,
    val he: String,
    val hi: String,
    val hr: String,
    val hu: String,
    val id: String,
    @SerializedName("`it`")
    val it: String,
    val ja: String,
    val ko: String,
    val lt: String,
    val nl: String,
    val no: String,
    val pl: String,
    val pt: String,
    val ro: String,
    val ru: String,
    val sk: String,
    val sl: String,
    val sv: String,
    val th: String,
    val tr: String,
    val uk: String,
    val vi: String,
    val zh: String,
    @SerializedName("zh-tw")
    val zh_tw: String
)