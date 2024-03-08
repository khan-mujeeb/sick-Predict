package com.example.sickpredict.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sickpredict.R
import com.example.sickpredict.databinding.ActivityDoctorListBinding

class DoctorListActivity : AppCompatActivity() {

    lateinit var binding: ActivityDoctorListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoctorListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        subscribeUi()
        subscribeClickListner()
    }

    private fun subscribeClickListner() {
        binding.dr1.setOnClickListener {
            startActivity(Intent(this, UserChatActivity::class.java))
        }

        binding.dr2.setOnClickListener {
            startActivity(Intent(this, UserChatActivity::class.java))
        }

        binding.dr3.setOnClickListener {
            startActivity(Intent(this, UserChatActivity::class.java))
        }
    }

    private fun subscribeUi() {
    }
}