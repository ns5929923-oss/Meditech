package com.example.meditechapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meditechapp.Doctor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        if (user != null) {

            val db = FirebaseFirestore.getInstance()

            db.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->

                    val role = document.getString("role")

                    if (role == "doctor") {
                        startActivity(Intent(this, DoctorDashboardActivity::class.java))
                    } else if (role == "hospital") {
                        startActivity(Intent(this, HospitalDashboardActivity::class.java))
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }

                    finish()
                }

        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}