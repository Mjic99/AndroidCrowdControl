package com.example.inocentemontemayorcrowdcontrol.models.beans

import com.google.android.gms.maps.model.LatLng

data class Location(
    val id: String,
    val name: String,
    val coordinates: LatLng,
    val currAttendance: Int,
    val maxCapacity: Int,
    val imageUrl: String)
