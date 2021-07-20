package com.example.inocentemontemayorcrowdcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.inocentemontemayorcrowdcontrol.models.beans.User
import com.example.inocentemontemayorcrowdcontrol.models.firebase.FirebaseUserDAO
import com.example.inocentemontemayorcrowdcontrol.models.firebase.OnGetUserDataDone
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity(), OnGetUserDataDone {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar = findViewById(R.id.progress_bar)
        // Setup
        setup()
    }

    private fun setup() {
        val loginButton = findViewById<AppCompatButton>(R.id.loginButton)
        val loginEmailET = findViewById<EditText>(R.id.loginEmailET)
        val loginPasswordET = findViewById<EditText>(R.id.loginPasswordET)
        val clientTV = findViewById<TextView>(R.id.clientTV)
        val companyTV = findViewById<TextView>(R.id.companyTV)
        title = "Autenticación"

        loginButton!!.setOnClickListener {
            if (loginEmailET!!.text.isNotEmpty() && loginPasswordET!!.text.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(loginEmailET.text.toString(), loginPasswordET.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            FirebaseUserDAO().getUserData(this)
                        } else {
                            showAlert()
                            progressBar.visibility = View.GONE
                        }
                    }
            }
        }

        clientTV.setOnClickListener{
            val registerIntent = Intent(this, RegisterActivity::class.java).apply {
                    putExtra("userType", "client")
            }
            startActivity(registerIntent)
        }
        companyTV.setOnClickListener{
            val registerIntent = Intent(this, RegisterActivity::class.java).apply {
                putExtra("userType", "company")
            }
            startActivity(registerIntent)
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error de Autenticación")
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
        startActivity(Intent(this, MyLocationsActivity::class.java))
    }

    override fun onUserDataSuccess(user: User) {
        if (user.type == "client") showClientActivity()
        if (user.type == "company") showCompanyActivity()
        progressBar.visibility = View.GONE
    }

}