package com.example.inocentemontemayorcrowdcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val bundle = intent.extras
        val userType = bundle!!.getString("userType")
        // Setup
        setup(userType!!)
    }

    private fun setup(userType: String) {

        val registerButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.registerButton)
        val registerEmailET = findViewById<EditText>(R.id.registerEmailET)
        val registerPasswordET = findViewById<EditText>(R.id.registerPasswordET)
        title = "Registro"
        registerButton!!.setOnClickListener {
            if (registerEmailET!!.text.isNotEmpty() && registerPasswordET!!.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(registerEmailET.text.toString(), registerPasswordET.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Obtenemos el ID del usuario recién creado
                            val userId : String = it.getResult()!!.user!!.uid
                            db.collection("users").document(userId).set(
                                hashMapOf("type" to userType)
                            )
                            if (userType == "client") {
                                showClientActivity()
                            }
                            else {
                                showCompanyActivity()
                            }
                        } else {
                            showAlert()
                        }
                    }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error de Registro")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showClientActivity() {
        val clientIntent = Intent(this, MapsActivity::class.java).apply {
        }
        startActivity(clientIntent)
    }

    private fun showCompanyActivity() {
        // TODO: Agregar cambio a actividad de empresa
        val clientIntent = Intent(this, MapsActivity::class.java).apply {
        }
        startActivity(clientIntent)
    }
}