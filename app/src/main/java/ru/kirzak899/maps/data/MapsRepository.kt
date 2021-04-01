package ru.kirzak899.maps.data

import android.location.Location

interface MapsRepository {

    fun getLocation(
        onComplete: (Location, List<LatLon>) -> Unit,
        onError: (Throwable) -> Unit
    )
}
