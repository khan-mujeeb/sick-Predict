package com.example.sickpredict.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sickpredict.R
import com.example.sickpredict.data.user.User
import com.example.sickpredict.databinding.ActivityPatientProfileBinding
import com.example.sickpredict.doctor.DoctorChatActivity
import com.google.firebase.auth.FirebaseAuth

class PatientProfileActivity : AppCompatActivity() {

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
            intent.putExtra("img", user.profile)
            startActivity(intent)

        }
    }

    private fun variableInitialization() {
        user = intent.getParcelableExtra("user")!!
    }


    private fun subscribeUi() {
        Glide.with(this)
            .load(
                user.profile
            )
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(binding.dpImage)

        FirebaseAuth.getInstance().currentUser!!.photoUrl

        print("mujeeb khan ${user.profile}")


        binding.patientName.text = user.fullname
        binding.age.text = user.dob
        binding.gender.text = user.gender
        binding.mobile.text = user.mobile
    }
}