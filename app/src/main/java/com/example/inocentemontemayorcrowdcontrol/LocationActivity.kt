package com.example.inocentemontemayorcrowdcontrol

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.example.inocentemontemayorcrowdcontrol.models.firebase.FirebaseLocationDAO
import com.example.inocentemontemayorcrowdcontrol.models.firebase.OnGetLocationDone


class LocationActivity : AppCompatActivity(), OnGetLocationDone {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        FirebaseLocationDAO().getLocation(intent.getStringExtra("id")!!, this)
    }

    override fun onLocationSuccess(location: Location) {
        findViewById<TextView>(R.id.location_name).text = location.name
        findViewById<TextView>(R.id.max_capacity_number).text = location.maxCapacity.toString()
        findViewById<TextView>(R.id.curr_attendance_number).text = location.currAttendance.toString()

        findViewById<Button>(R.id.open_route).setOnClickListener {
            val uri = "geo:0,0?q=${location.coordinates.latitude},${location.coordinates.longitude}(${java.net.URLEncoder.encode(location.name, "utf-8")})"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }

        Glide.with(this).load(location.imageUrl)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(findViewById(R.id.location_image))
    }
}