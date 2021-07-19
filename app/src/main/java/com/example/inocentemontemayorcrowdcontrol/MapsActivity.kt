package com.example.inocentemontemayorcrowdcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.example.inocentemontemayorcrowdcontrol.models.firebase.FirebaseLocationDAO
import com.example.inocentemontemayorcrowdcontrol.models.firebase.OnGetLocationsDone

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnGetLocationsDone {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        FirebaseLocationDAO().getLocations(this)

        mMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(this, LocationActivity::class.java)
            intent.putExtra("id", marker.tag as String)
            startActivity(intent)
        }
    }

    private fun getColor(location: Location): Float {
        val ratio = location.currAttendance.toDouble() / location.maxCapacity.toDouble()

        return when {
            ratio < 0.33 -> {
                BitmapDescriptorFactory.HUE_GREEN
            }
            ratio < 0.66 -> {
                BitmapDescriptorFactory.HUE_YELLOW
            }
            else -> {
                BitmapDescriptorFactory.HUE_RED
            }
        }
    }

    override fun onLocationsSuccess(locations: List<Location>) {
        val boundsBuilder = LatLngBounds.builder()

        for (location in locations) {
            val marker = mMap.addMarker(MarkerOptions()
                .icon(BitmapDescriptorFactory
                    .defaultMarker(getColor(location)))
                .position(location.coordinates)
                .title(location.name))

            marker.tag = location.id

            boundsBuilder.include(location.coordinates)
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(boundsBuilder.build().center, 13.5f))
    }

    override fun onError(msg: String) {
        Log.i("firebase", msg)
    }
}