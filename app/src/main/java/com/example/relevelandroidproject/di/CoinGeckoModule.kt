package com.example.relevelandroidproject.di

import com.example.relevelandroidproject.data.data_source.dto.CoinGeckoApi
import com.example.relevelandroidproject.data.repository.CoinRepositoryImpl
import com.example.relevelandroidproject.domain.repository.CoinRepository
import com.example.relevelandroidproject.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoinGeckoModule {

    @Provides
    @Singleton
    fun provideJokesApi(): CoinGeckoApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinGeckoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinGeckoRepository(api:CoinGeckoApi):CoinRepository{
        return CoinRepositoryImpl(api=api)
    }
}