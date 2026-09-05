package com.example.meditechapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class CaseUploadActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_upload)

        findViewById<Button>(R.id.btnUpload).setOnClickListener {
            uploadCase()
        }
    }

    private fun uploadCase() {

        val doctorId = "doctor123"
        val currentCount = 1 // fetch from DB in real app

        if (currentCount >= 2) {
            Toast.makeText(this, "Free limit reached", Toast.LENGTH_SHORT).show()
            return
        }

        val case = hashMapOf(
            "caseId" to db.collection("cases").document().id,
            "doctorId" to doctorId,
            "fileUrl" to "sample_url",
            "description" to "Heart case study"
        )

        db.collection("cases").add(case)

        Toast.makeText(this, "Case Uploaded", Toast.LENGTH_SHORT).show()
    }
}