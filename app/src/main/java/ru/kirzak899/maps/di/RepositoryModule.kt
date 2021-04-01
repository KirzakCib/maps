package ru.kirzak899.maps.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.kirzak899.maps.data.MapsRepository
import ru.kirzak899.maps.data.MapsRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    fun providesMapsRepository(mapsRepositoryImpl: MapsRepositoryImpl): MapsRepository {
        return mapsRepositoryImpl
    }

    @Provides
    fun providesFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application)
    }
}
