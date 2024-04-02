package com.example.sickpredict.doctor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sickpredict.databinding.ActivityDoctorProfileBinding
import com.example.sickpredict.user.UserChatActivity
import com.example.sickpredict.utils.DoctorData
import com.google.android.material.chip.Chip

class DoctorProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorProfileBinding
    private var doctorUid = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        variableInitialization()
        subscribeUi()
        subscribeClickListner()


    }

    private fun subscribeClickListner() {
        binding.chat.setOnClickListener {
            val intent = Intent(this, UserChatActivity::class.java)
            intent.putExtra("from", "user")

            intent.putExtra("name", DoctorData.doctorList[doctorUid].name)
            intent.putExtra("uid", DoctorData.doctorList[doctorUid].uid)
            startActivity(intent)
        }
    }

    private fun subscribeUi() {
        binding.drName.text = DoctorData.doctorList[doctorUid].name
        binding.qualification.text = DoctorData.doctorList[doctorUid].qualification
        binding.exp.text = DoctorData.doctorList[doctorUid].experience  + " exp"
        DoctorData.doctorList[doctorUid].specalization.forEach {
            addChip(it)
        }
    }

    private fun addChip(text: String?) {
        text?.let {
            val chip = Chip(this)
            chip.text = it
            chip.isCloseIconVisible = true
            chip.isClickable = false
            chip.isCheckable = false
           binding.specialization.addView(chip)
        }
    }

    private fun variableInitialization() {
        doctorUid = intent.getIntExtra("doctorId", 0)

    }
}