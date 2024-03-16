package com.example.sickpredict.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sickpredict.R
import com.example.sickpredict.adapter.MedicineAdapter
import com.example.sickpredict.data.prediction.PreductionResult
import com.example.sickpredict.databinding.ActivityPredictionResultBinding

class PredictionResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictionResultBinding
    lateinit var prediction: PreductionResult
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPredictionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        variableInitialization()
        subscribeUi()
        subscribeListeners()

    }

    private fun subscribeUi() {

        println("wini ${prediction.prediction} ${prediction.accuracy} ${prediction.medcines}")
        binding.accuracy.text = prediction.accuracy
        binding.deseaseName.text = prediction.prediction

        if(prediction.medcines.isEmpty()) {
            binding.recyclerViewSuggestedMedicines.visibility = View.GONE
            binding.noMedicinesFound.visibility = View.VISIBLE
        } else {
            print("pillu ${prediction.medcines.size}")
            binding.recyclerViewSuggestedMedicines.visibility = View.VISIBLE
            binding.recyclerViewSuggestedMedicines.adapter = MedicineAdapter(prediction.medcines)
            binding.noMedicinesFound.visibility = View.GONE
        }
    }

    private fun subscribeListeners() {

        binding.getAccuracy.setOnClickListener {
            binding.getAccuracy.visibility = View.GONE
            binding.accuracy.visibility = View.VISIBLE
        }

        binding.consultDoctor.setOnClickListener {
            startActivity(Intent(this, DoctorListActivity::class.java))
        }
    }

    private fun variableInitialization() {
         prediction = intent.getSerializableExtra("result") as PreductionResult


    }
}