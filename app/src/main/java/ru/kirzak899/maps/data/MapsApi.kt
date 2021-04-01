package ru.kirzak899.maps.data

import android.location.Location
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApi {

    @GET("lookup?entity=location")
    fun getLocation(
        @Query("location") location: Location
    ): Call<ServerItemsWrapper<LatLon>>

}
