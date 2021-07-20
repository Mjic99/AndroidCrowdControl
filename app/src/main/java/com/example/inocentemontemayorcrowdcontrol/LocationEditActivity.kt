package com.example.inocentemontemayorcrowdcontrol

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.example.inocentemontemayorcrowdcontrol.models.firebase.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint


class LocationEditActivity: AppCompatActivity(), OnGetLocationDone, OnMapReadyCallback, GoogleMap.OnMapClickListener, OnUploadImageDone, OnUploadLocationDone {

    private lateinit var location: Location
    private lateinit var marker: Marker
    private var imagePicker : ImageView? = null
    private var imageURI : Uri? = null
    private var locationID : String? = null
    private var updateOrCreate : Boolean? = null
    private var isNewImage : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_location_edit)

        updateOrCreate = intent.getBooleanExtra("update", false)
        if (updateOrCreate!!) {
            locationID = intent.getStringExtra("id")!!
            FirebaseLocationDAO().getLocation(locationID!!, this)
        } else {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.minimap) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }


        imagePicker = findViewById(R.id.locationImage)
        findViewById<Button>(R.id.chooseImageButton).setOnClickListener {
            ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*")).crop().start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode== Activity.RESULT_OK && requestCode==ImagePicker.REQUEST_CODE) {
            imagePicker!!.setImageURI(data?.data)
            imageURI = data?.data
            isNewImage = true
        }
    }





    override fun onLocationSuccess(location: Location) {
        this.location = location
        findViewById<EditText>(R.id.LocationNameET).setText(location.name)
        findViewById<EditText>(R.id.MaxAttendanceET).setText(location.maxCapacity.toString())

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

        if (updateOrCreate!!) {
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(location.coordinates)
                    .draggable(true))

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location.coordinates, 12f))
        } else {
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(-12.124109061474547,-76.99732560664415))
                    .draggable(true))

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-12.124109061474547,-76.99732560664415), 12f))
            Log.i("mapa", "si entra a la funcion")
        }

        findViewById<Button>(R.id.sendButton).setOnClickListener {
            if (isNewImage && updateOrCreate!!) {
                FirebaseStorageDAO().uploadImage(imageURI!!, this)
            }
            else if (isNewImage && !updateOrCreate!!){
                FirebaseStorageDAO().uploadImage(imageURI!!, this)
            }
            else if (!isNewImage && updateOrCreate!!) {
                handleUploadLocation(location.imageUrl)
            }
            else {
                handleUploadLocation("https://firebase.google.com/images/brand-guidelines/logo-built_black.png")
            }
        }

    }

    override fun onMapClick(p0: LatLng?) {
        marker.position = p0
    }

    override fun onUploadImageSuccess(imageURL: String) {
        handleUploadLocation(imageURL)
    }

    fun handleUploadLocation(imageURL: String) {
        if (updateOrCreate!!) {
            FirebaseLocationDAO().updateLocation(
                locationID!!,
                GeoPoint(marker.position.latitude, marker.position.longitude),
                findViewById<EditText>(R.id.LocationNameET).text.toString(),
                findViewById<EditText>(R.id.MaxAttendanceET).text.toString().toInt(),
                imageURL,this)
        } else {
            FirebaseLocationDAO().createLocation(
                GeoPoint(marker.position.latitude, marker.position.longitude),
                findViewById<EditText>(R.id.LocationNameET).text.toString(),
                findViewById<EditText>(R.id.MaxAttendanceET).text.toString().toInt(),
                imageURL, FirebaseAuth.getInstance().currentUser!!.uid, this)
        }
    }


    override fun onUploadImageError(msg: String) {
        Toast.makeText(applicationContext,"Error al subir la imagen",Toast.LENGTH_SHORT).show()
    }

    override fun onUploadSuccess() {
        Toast.makeText(applicationContext,"Establecimiento Subido",Toast.LENGTH_SHORT).show()
    }
}