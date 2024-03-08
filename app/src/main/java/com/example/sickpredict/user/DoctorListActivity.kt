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
        val intent = Intent(this, UserChatActivity::class.java)
        binding.dr1.setOnClickListener {
            intent.putExtra("name", "Dr. Isha Kalbhor")
            intent.putExtra("uid", "")
            startActivity(intent)
        }

        binding.dr2.setOnClickListener {
            intent.putExtra("name", "Dr. Wini Rasam")
            intent.putExtra("uid", "")
            startActivity(intent)
        }

        binding.dr3.setOnClickListener {
            intent.putExtra("name", "Dr. H. Khalate")
            intent.putExtra("uid", "sZkJJFsmlldSjr2iawsu38IJT612")
            startActivity(intent)
        }
    }

    private fun subscribeUi() {
    }
}