package com.example.inocentemontemayorcrowdcontrol.models.firebase

import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore

interface OnGetLocationsDone {
    fun onLocationsSuccess(locations : List<Location>)
    fun onError(msg : String)
}

class FirebaseLocationDAO {

    private val db = FirebaseFirestore.getInstance()

    fun getLocations(callback : OnGetLocationsDone) {
        db.collection("locations")
            .get()
            .addOnSuccessListener { documents ->
                val locations = documents.map { doc ->
                    Location(
                        doc.id,
                        doc.data["name"].toString(),
                        LatLng(
                            doc.getGeoPoint("coordinates")!!.latitude,
                            doc.getGeoPoint("coordinates")!!.longitude
                        ),
                        doc.data["curr_attendance"].toString().toInt(),
                        doc.data["max_capacity"].toString().toInt()
                    )
                }
                callback.onLocationsSuccess(locations)
            }.addOnFailureListener { exception ->
                callback.onError(exception.message!!)
            }
    }
}