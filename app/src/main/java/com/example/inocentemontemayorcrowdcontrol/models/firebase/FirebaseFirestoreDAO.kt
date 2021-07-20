package com.example.inocentemontemayorcrowdcontrol.models.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage


interface OnUploadImageDone {
    fun onUploadImageSuccess(imageURL : String)
    fun onUploadImageError(msg : String)
}

class FirebaseStorageDAO {

    private val storage = FirebaseStorage.getInstance()

    fun uploadImage(imageUri : Uri, callback : OnUploadImageDone) {
        storage.getReference("images/").putFile(imageUri).
                addOnSuccessListener { image ->
                    image.storage.downloadUrl.addOnSuccessListener {
                        callback.onUploadImageSuccess(it.toString())
                    }

                }.addOnFailureListener { exception ->
                    callback.onUploadImageError(exception.message!!)
                }
    }
}
