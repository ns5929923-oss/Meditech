package com.example.meditechapp.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.meditechapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class JobDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        // ✅ Get data safely
        val title = intent.getStringExtra("title") ?: ""
        val location = intent.getStringExtra("location") ?: ""
        val description = intent.getStringExtra("description") ?: ""

        // ✅ Set data
        findViewById<TextView>(R.id.title).text = title
        findViewById<TextView>(R.id.location).text = location
        findViewById<TextView>(R.id.description).text = description

        // ✅ Apply Button
        val applyBtn = findViewById<Button>(R.id.applyBtn)

        applyBtn.setOnClickListener {

            val db = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()

            val doctorId = auth.currentUser?.uid

            if (doctorId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val application = hashMapOf(
                "jobTitle" to title,
                "doctorId" to doctorId,
                "location" to location,
                "appliedAt" to System.currentTimeMillis()
            )

            db.collection("applications")
                .add(application)
                .addOnSuccessListener {
                    Toast.makeText(this, "Applied Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to apply", Toast.LENGTH_SHORT).show()
                }
        }
    }
}