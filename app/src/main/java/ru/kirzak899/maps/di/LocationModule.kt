package ru.kirzak899.maps.di

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.kirzak899.maps.R

@Module
@InstallIn(ViewModelComponent::class)
class LocationModule {

    @Provides
    fun providesMapBox(application: Application): Mapbox {
        return Mapbox.getInstance(application, application.getString(R.string.mapbox_access_token))
    }
}
