package com.example.sickpredict.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sickpredict.R
import com.example.sickpredict.data.prediction.PreductionResult

class PatientRecordAdapter(private val patientRecords: List<PreductionResult>) :
    RecyclerView.Adapter<PatientRecordAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        val predictionTextView: TextView = itemView.findViewById(R.id.predictionTextView)
        val accuracyTextView: TextView = itemView.findViewById(R.id.accuracyTextView)
        val symptomsTextView: TextView = itemView.findViewById(R.id.symptomsTextView)
        val medicinesTextView: TextView = itemView.findViewById(R.id.medicinesTextView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_patient_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = patientRecords[position]

        holder.timestampTextView.text = if (record.timestamp.isNotEmpty()) {
            record.timestamp.slice(0..15)
        } else {
            "No timestamp"
        }
        holder.predictionTextView.text = "${record.prediction}"
        holder.accuracyTextView.text = "Accuracy: ${record.accuracy}"
        holder.symptomsTextView.text = "Symptoms: ${record.symtomps.joinToString(", ")}"
        holder.medicinesTextView.text = "Medicines: ${record.medcines.joinToString(", ")}"
    }

    override fun getItemCount(): Int {
        return patientRecords.size
    }
}

