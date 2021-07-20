package com.example.inocentemontemayorcrowdcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.inocentemontemayorcrowdcontrol.adapters.LocationsAdapter
import com.example.inocentemontemayorcrowdcontrol.adapters.OnLocationItemClickListener
import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.example.inocentemontemayorcrowdcontrol.models.firebase.FirebaseLocationDAO
import com.example.inocentemontemayorcrowdcontrol.models.firebase.OnGetLocationsDone
import com.example.inocentemontemayorcrowdcontrol.models.firebase.OnUpdateLocationAttendanceDone

class MyLocationsActivity : AppCompatActivity(), OnGetLocationsDone, OnLocationItemClickListener, OnUpdateLocationAttendanceDone {

    var locationsView: RecyclerView? = null
    private lateinit var locations: List<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_locations)
        locationsView = findViewById(R.id.locations_list)
        FirebaseLocationDAO().getUserLocations(this)
    }

    override fun onLocationsSuccess(locations: List<Location>) {
        this.locations = locations
        runOnUiThread {
            locationsView!!.adapter = LocationsAdapter(this.locations, this, this)
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
        FirebaseLocationDAO().setLocationAttendance(location.id, location.currAttendance + 10, this)
    }

    override fun onMinusClick(location: Location) {
        FirebaseLocationDAO().setLocationAttendance(location.id, location.currAttendance - 10, this)
    }

    override fun onLocationAttendanceUpdated(id: String, attendance: Int) {
        val index = locations.indexOfFirst { location -> location.id == id }
        locations[index].currAttendance = attendance
        locationsView!!.adapter!!.notifyItemChanged(index)
    }
}