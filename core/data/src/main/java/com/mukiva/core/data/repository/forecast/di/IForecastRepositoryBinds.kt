package com.mukiva.core.data.repository.forecast.di

import com.mukiva.core.data.repository.forecast.IForecastRepository
import com.mukiva.core.data.repository.forecast.impl.ForecastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IForecastRepositoryBinds {

    @Binds
    fun bindCoreForecastRepository(
        repo: ForecastRepositoryImpl
    ) : IForecastRepository

}