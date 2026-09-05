package com.example.meditechapp

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ApplicantAdapter(
    private val list: List<Application>
) : RecyclerView.Adapter<ApplicantAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val t1: TextView = view.findViewById(android.R.id.text1)
        val t2: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.t1.text = item.doctorId
        holder.t2.text = item.status
    }

    override fun getItemCount() = list.size
}