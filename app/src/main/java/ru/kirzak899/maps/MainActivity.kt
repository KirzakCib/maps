package ru.kirzak899.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import dagger.hilt.android.AndroidEntryPoint
import ru.kirzak899.maps.ui.MapsModelView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MapsModelView by viewModels()

    private var rationalDialog: AlertDialog? = null
    private var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_main)
        Mapbox.getInstance(application, application.getString(R.string.mapbox_access_token))

        setContentView(R.layout.activity_main)

        val isLocationPermissionGranted = ActivityCompat
            .checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (isLocationPermissionGranted) {
            viewModel.get()
        } else {
            val needRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (needRationale) {
                showLocationRationalDialog()
            } else {
                requestLocationPermission()
            }
        }

        mapView = findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)

        viewModel.location.observe(this) {
            Log.d("qqqqq", it.toString())
            mapView?.getMapAsync { mapboxMap ->
                val position = CameraPosition.Builder()
                    .target(LatLng(it.latitude, it.longitude))
                    .zoom(10.0)
                    .tilt(20.0)
                    .build()

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 100)

                mapboxMap.setStyle(
                    Style.Builder().fromUri("mapbox://styles/kirzak98/ckmxkrjbx0x5217p644sd8vow")
                        .withImage(
                            ICON_ID, BitmapFactory.decodeResource(
                                resources, R.drawable.mapbox_marker_icon_default
                            )
                        )
                        .withSource(
                            GeoJsonSource(
                                SOURCE_ID,
                                Feature.fromGeometry(Point.fromLngLat(it.longitude, it.latitude))
                            )
                        )
                        .withLayer(
                            SymbolLayer(LAYER_ID, SOURCE_ID)
                                .withProperties(
                                    iconImage(ICON_ID),
                                    iconAllowOverlap(true),
                                    iconIgnorePlacement(true)
                                )
                        )
                )

                viewModel.latlog.observe(this) {
                    it.forEach {
                        mapboxMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(it.latitude, it.longitude))
                                .icon(
                                    IconFactory.getInstance(this)
                                        .fromResource(R.drawable.mapbox_marker_icon_default)
                                )
                        )
                    }
                }
            }
        }

        viewModel.toast.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
        rationalDialog?.dismiss()
        rationalDialog = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            viewModel.get()
            // showLocationInfo()
        } else {
            val needRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (needRationale) {
                showLocationRationalDialog()
            }
        }
    }

    private fun showLocationRationalDialog() {
        rationalDialog = AlertDialog.Builder(this)
            .setMessage("Необходимо одобрение разрешение на отображение информации о локации")
            .setPositiveButton("ОК") { _, _ -> requestLocationPermission() }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1312
        private const val SOURCE_ID = "SOURCE_ID"
        private const val ICON_ID = "ICON_ID"
        private const val LAYER_ID = "LAYER_ID"
    }
}
