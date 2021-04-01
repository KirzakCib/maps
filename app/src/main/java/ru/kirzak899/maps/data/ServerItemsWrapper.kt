package ru.kirzak899.maps.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServerItemsWrapper<T>(
    val location: List<T>
)
