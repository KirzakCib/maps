package ru.kirzak899.maps.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LatLon(
    val latitude: Double,
    val longitude: Double
)
