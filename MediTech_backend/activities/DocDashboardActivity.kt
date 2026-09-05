package com.example.meditechapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meditechapp.Job
import com.example.meditechapp.JobAdapter
import com.example.meditechapp.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class DoctorDashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var jobList: ArrayList<Job>
    private lateinit var adapter: JobAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_dashboard)

        // 🔷 RecyclerView Setup
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        jobList = ArrayList()

        adapter = JobAdapter(jobList) { job ->

            val intent = Intent(this, JobDetailsActivity::class.java)

            intent.putExtra("title", job.title)
            intent.putExtra("location", job.location)
            intent.putExtra("description", job.description)

            startActivity(intent)
        }

        recyclerView.adapter = adapter

        // 🔷 Firebase
        db = FirebaseFirestore.getInstance()

        // 🔷 Spinner Setup (DO NOT REMOVE - FIXED)
        val spinner = findViewById<Spinner>(R.id.filterSpinner)

        val options = listOf("All", "Cardiology", "Dental", "Neurology")

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            options
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // 🔷 Spinner Listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val selected = parent.getItemAtPosition(position).toString()

                if (selected == "All") {
                    fetchJobs()
                } else {
                    filterJobs(selected)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // 🔷 Load all jobs initially
        fetchJobs()
    }

    // 🔷 Fetch all jobs
    private fun fetchJobs() {

        db.collection("jobs")
            .get()
            .addOnSuccessListener { result ->

                jobList.clear()

                for (document in result) {
                    val job = document.toObject(Job::class.java)
                    jobList.add(job)
                }

                adapter.notifyDataSetChanged()
            }
    }

    // 🔷 Filter jobs
    private fun filterJobs(specialization: String) {

        db.collection("jobs")
            .whereEqualTo("specialization", specialization)
            .get()
            .addOnSuccessListener { result ->

                jobList.clear()

                for (document in result) {
                    val job = document.toObject(Job::class.java)
                    jobList.add(job)
                }

                adapter.notifyDataSetChanged()
            }
    }
}