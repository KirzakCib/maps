package ru.kirzak899.maps.data

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class MapsRepositoryImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val mapsApi: MapsApi
) : MapsRepository {

    @SuppressLint("MissingPermission")
    override fun getLocation(
        onComplete: (Location, List<LatLon>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        fusedLocationProviderClient
            .lastLocation
            .addOnSuccessListener {
                it?.let {
                    Log.d("qqqqqqqqqqq", it.toString())
                    mapsApi.getLocation(it).enqueue(
                        object : retrofit2.Callback<ServerItemsWrapper<LatLon>> {
                            override fun onResponse(
                                call: Call<ServerItemsWrapper<LatLon>>,
                                response: Response<ServerItemsWrapper<LatLon>>
                            ) {
                                if (response.isSuccessful) {
                                    val location = response.body()?.location

                                    if (location != null) {
                                        onComplete(it, location)
                                    }

                                } else {
                                    onComplete(
                                        it, listOf(
                                            LatLon(55.0400, 82.9590),
                                            LatLon(55.0590, 82.9988)
                                        )
                                    )
                                }
                            }

                            override fun onFailure(
                                call: Call<ServerItemsWrapper<LatLon>>,
                                t: Throwable
                            ) {
                                onError(RuntimeException("Error, check your network connection. Regards."))
                            }

                        }
                    )
                }
            }
    }
}
