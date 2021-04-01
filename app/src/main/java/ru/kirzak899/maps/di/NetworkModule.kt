package ru.kirzak899.maps.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.kirzak899.maps.data.MapsApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): Retrofit = Retrofit.Builder()
        .baseUrl("https://s3-eu-west-1.amazonaws.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(OkHttpClient())
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(retrofit: Retrofit): MapsApi = retrofit.create()
}
