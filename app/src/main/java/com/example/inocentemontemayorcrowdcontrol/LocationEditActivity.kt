package com.example.inocentemontemayorcrowdcontrol

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.example.inocentemontemayorcrowdcontrol.models.firebase.FirebaseLocationDAO
import com.example.inocentemontemayorcrowdcontrol.models.firebase.OnGetLocationDone
import com.google.firebase.auth.FirebaseAuth

class LocationEditActivity: AppCompatActivity(), OnGetLocationDone {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_edit)
        FirebaseLocationDAO().getLocation(intent.getStringExtra("id")!!, this)
    }


    override fun onLocationSuccess(location: Location) {
        findViewById<EditText>(R.id.LocationNameET).setText(location.name)

        Glide.with(this).load(location.imageUrl)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(findViewById(R.id.locationImage))

    }
}