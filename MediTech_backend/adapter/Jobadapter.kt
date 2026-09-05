package com.example.meditechapp

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class JobAdapter(
    private val jobList: List<Job>,
    private val onClick: (Job) -> Unit   // ✅ click listener added
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    class JobViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val location: TextView = view.findViewById(R.id.location)
        val experience: TextView = view.findViewById(R.id.experience)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_item, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobList[position]

        holder.title.text = job.title
        holder.location.text = job.location
        holder.experience.text = "Exp: ${job.experience} years"

        // ✅ HANDLE CLICK HERE
        holder.itemView.setOnClickListener {
            onClick(job)
        }
    }

    override fun getItemCount(): Int = jobList.size
}