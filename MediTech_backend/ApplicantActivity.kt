package com.example.meditechapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class ApplicantActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val list = ArrayList<com.example.meditechapp.Application>()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicant)
        recyclerView = findViewById(R.id.recyclerApplicants)
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadApplicants()
    }

    private fun loadApplicants() {

        val jobId = "job123"

        db.collection("applications")
            .whereEqualTo("jobId", jobId)
            .get()
            .addOnSuccessListener { result ->

                list.clear()

                for (doc in result) {
                    list.add(doc.toObject(com.example.meditechapp.Application::class.java))
                }

                recyclerView.adapter = ApplicantAdapter(list)

            }
    }
}