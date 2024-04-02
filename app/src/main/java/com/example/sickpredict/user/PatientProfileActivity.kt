package com.example.sickpredict.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.sickpredict.R
import com.example.sickpredict.adapter.PatientRecordAdapter
import com.example.sickpredict.data.user.User
import com.example.sickpredict.databinding.ActivityPatientProfileBinding
import com.example.sickpredict.doctor.DoctorChatActivity
import com.example.sickpredict.repository.ViewModel
import com.example.sickpredict.utils.DialogUtils
import com.example.sickpredict.utils.FirebaseUtils
import com.example.sickpredict.utils.getCurrentYear
import com.google.firebase.auth.FirebaseAuth

class PatientProfileActivity : AppCompatActivity() {

    lateinit var viewModel: ViewModel
    lateinit var alertDialog: AlertDialog
    lateinit var binding: ActivityPatientProfileBinding
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        variableInitialization()
        subscribeUi()
        subscribeCLickEvents()
    }

    private fun subscribeCLickEvents() {
        binding.call.setOnClickListener {

        }


        binding.chat.setOnClickListener {
            val intent = Intent(this, DoctorChatActivity::class.java)
            intent.putExtra("patientUid", user.uid)
            intent.putExtra("patientName", user.fullname)
            intent.putExtra("patientProfile", user.profile)
            startActivity(intent)

        }
    }

    private fun variableInitialization() {
        user = intent.getParcelableExtra("user")!!
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        alertDialog = DialogUtils.buildLoadingDialog(this)
    }


    private fun subscribeUi() {

        alertDialog.show()

        Glide.with(this)
            .load(
                user.profile
            )
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(binding.dpImage)

        FirebaseAuth.getInstance().currentUser!!.photoUrl


        val reversedDob = user.dob.reversed()
        val year = reversedDob.take(4).reversed().toInt()
        val age = getCurrentYear() - year

        binding.patientName.text = user.fullname
        binding.age.text = age.toString()
        binding.gender.text = user.gender
        binding.mobile.text = user.mobile


        getPaitientHistory()

    }

    private fun getPaitientHistory() {
        viewModel.readPatientRecord(FirebaseUtils.firebaseUser!!.uid, user.uid) { patientRecord ->
            // Handle the patient record here
            if (patientRecord != null) {
                binding.recordsRc.visibility = View.VISIBLE
                binding.noRecord.visibility = View.GONE
                println("Patient Record: $patientRecord")
                binding.recordsRc.adapter = PatientRecordAdapter(patientRecord)

            } else {

                binding.recordsRc.visibility = View.GONE
                binding.noRecord.visibility = View.VISIBLE

            }

            alertDialog.hide()
        }
    }
}