package com.example.inocentemontemayorcrowdcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.inocentemontemayorcrowdcontrol.adapters.LocationsAdapter
import com.example.inocentemontemayorcrowdcontrol.adapters.OnLocationItemClickListener
import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.example.inocentemontemayorcrowdcontrol.models.firebase.FirebaseLocationDAO
import com.example.inocentemontemayorcrowdcontrol.models.firebase.OnGetLocationsDone

class MyLocationsActivity : AppCompatActivity(), OnGetLocationsDone, OnLocationItemClickListener {

    var locationsView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_locations)
        locationsView = findViewById(R.id.locations_list)
        FirebaseLocationDAO().getUserLocations(this)
    }

    override fun onLocationsSuccess(locations: List<Location>) {
        runOnUiThread {
            locationsView!!.adapter = LocationsAdapter(locations, this, this)
        }
    }

    override fun onError(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(location: Location) {
        val intent = Intent(this, LocationEditActivity::class.java)
        intent.putExtra("id",  location.id)
        startActivity(intent)
    }

    override fun onPlusClick(location: Location) {
        Toast.makeText(this, "mas", Toast.LENGTH_SHORT).show()
    }

    override fun onMinusClick(location: Location) {
        Toast.makeText(this, "menos", Toast.LENGTH_SHORT).show()
    }
}