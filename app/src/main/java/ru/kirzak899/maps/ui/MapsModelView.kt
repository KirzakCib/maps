package ru.kirzak899.maps.ui

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kirzak899.maps.data.LatLon
import ru.kirzak899.maps.data.MapsRepository
import javax.inject.Inject

@HiltViewModel
class MapsModelView @Inject constructor(
    private val repository: MapsRepository
) : ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()
    private val latlogLiveData = MutableLiveData<List<LatLon>>()
    private val tostLiveData = MutableLiveData<String>()

    val location: LiveData<Location>
        get() = locationLiveData

    val latlog: LiveData<List<LatLon>>
        get() = latlogLiveData

    val toast: LiveData<String>
        get() = tostLiveData

    fun get() {
        repository.getLocation(
            onComplete = { location, latlng ->
                locationLiveData.postValue(location)
                latlogLiveData.postValue(latlng)
            },
            onError = { throwable ->
                tostLiveData.postValue(throwable.message)
            }
        )
    }
}
