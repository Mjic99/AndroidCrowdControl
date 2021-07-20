package com.example.inocentemontemayorcrowdcontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.example.inocentemontemayorcrowdcontrol.models.firebase.FirebaseLocationDAO
import com.example.inocentemontemayorcrowdcontrol.models.firebase.OnGetLocationDone
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

class LocationEditActivity: AppCompatActivity(), OnGetLocationDone, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var location: Location
    private lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_edit)
        FirebaseLocationDAO().getLocation(intent.getStringExtra("id")!!, this)

        findViewById<Button>(R.id.sendButton).setOnClickListener {
            // para obtener en cualquier momento el lugar donde el usuario dejo el marcador solo usa marker.position
            Toast.makeText(this, "${marker.position.longitude}, ${marker.position.latitude}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLocationSuccess(location: Location) {
        this.location = location
        findViewById<EditText>(R.id.LocationNameET).setText(location.name)

        Glide.with(this).load(location.imageUrl)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(findViewById(R.id.locationImage))

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.minimap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setOnMapClickListener(this)

        googleMap.setOnCameraMoveStartedListener {
            findViewById<ScrollView>(R.id.scroll).requestDisallowInterceptTouchEvent(true)
        }

        googleMap.setOnCameraIdleListener {
            findViewById<ScrollView>(R.id.scroll).requestDisallowInterceptTouchEvent(false)
        }

        marker = googleMap.addMarker(
            MarkerOptions()
                .position(location.coordinates)
                .draggable(true))

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location.coordinates, 12f))
    }

    override fun onMapClick(p0: LatLng?) {
        marker.position = p0
    }
}