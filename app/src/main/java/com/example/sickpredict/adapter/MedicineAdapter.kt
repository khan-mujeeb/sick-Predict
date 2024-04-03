package com.example.sickpredict.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sickpredict.R

class MedicineAdapter(private val medicines: List<String>) :
    RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        println("wini ${medicines[position]}")
        if (medicines.isNotEmpty())
            holder.bind(medicines[position])
    }

    override fun getItemCount(): Int {
        return medicines.size
    }

    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val medicineNameTextView: TextView = itemView.findViewById(R.id.medicineNameTextView)

        fun bind(medicine: String) {
            medicineNameTextView.text = medicine
        }
    }
}
