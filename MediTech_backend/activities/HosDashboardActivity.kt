package com.example.meditechapp.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meditechapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HospitalDashboardActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hos_dashboard)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    fun postJob(view: View) {

        val title = findViewById<EditText>(R.id.title).text.toString()
        val specialization = findViewById<EditText>(R.id.specialization).text.toString()
        val experience = findViewById<EditText>(R.id.experience).text.toString()
        val location = findViewById<EditText>(R.id.location).text.toString()
        val description = findViewById<EditText>(R.id.description).text.toString()

        val hospitalId = auth.currentUser?.uid

        val job = hashMapOf(
            "title" to title,
            "specialization" to specialization,
            "experience" to experience,
            "location" to location,
            "description" to description,
            "hospitalId" to hospitalId,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("jobs")
            .add(job)
            .addOnSuccessListener {
                Toast.makeText(this, "Job Posted Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error posting job", Toast.LENGTH_SHORT).show()
            }


    }
}
