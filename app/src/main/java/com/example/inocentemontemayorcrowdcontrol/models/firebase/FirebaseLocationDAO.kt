package com.example.inocentemontemayorcrowdcontrol.models.firebase

import android.util.Log
import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface OnGetLocationsDone {
    fun onLocationsSuccess(locations : List<Location>)
    fun onError(msg : String)
}

interface OnGetLocationDone {
    fun onLocationSuccess(location : Location)
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
                        doc.data["max_capacity"].toString().toInt(),
                        doc.data["image_url"].toString()
                    )
                }
                callback.onLocationsSuccess(locations)
            }.addOnFailureListener { exception ->
                callback.onError(exception.message!!)
            }
    }

    fun getLocation(id: String, callback : OnGetLocationDone) {
        db.collection("locations")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                callback.onLocationSuccess(Location(
                    document.id,
                    document["name"].toString(),
                    LatLng(
                        document.getGeoPoint("coordinates")!!.latitude,
                        document.getGeoPoint("coordinates")!!.longitude
                    ),
                    document["curr_attendance"].toString().toInt(),
                    document["max_capacity"].toString().toInt(),
                    document["image_url"].toString()
                ))
            }.addOnFailureListener { exception ->
                Log.i("firebase", exception.message!!)
            }
    }

    fun getUserLocations(callback : OnGetLocationsDone) {
        db.collection("locations")
            .whereEqualTo("owner", FirebaseAuth.getInstance().currentUser!!.uid)
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
                        doc.data["max_capacity"].toString().toInt(),
                        doc.data["image_url"].toString()
                    )
                }
                callback.onLocationsSuccess(locations)
            }.addOnFailureListener { exception ->
                callback.onError(exception.message!!)
            }
    }
}