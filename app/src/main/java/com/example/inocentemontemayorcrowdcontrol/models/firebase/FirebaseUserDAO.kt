package com.example.inocentemontemayorcrowdcontrol.models.firebase

import com.example.inocentemontemayorcrowdcontrol.models.beans.Location
import com.example.inocentemontemayorcrowdcontrol.models.beans.User
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface OnGetUserDataDone {
    fun onUserDataSuccess(user: User)
}

class FirebaseUserDAO {
    private val db = FirebaseFirestore.getInstance()

    fun getUserData(callback : OnGetUserDataDone) {
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                callback.onUserDataSuccess(User(document["type"].toString()))
            }
    }
}