package com.example.sickpredict.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sickpredict.R
import com.example.sickpredict.data.prediction.PreductionResult
import com.example.sickpredict.databinding.ActivityDoctorListBinding
import com.example.sickpredict.utils.DoctorData

class DoctorListActivity : AppCompatActivity() {

    lateinit var binding: ActivityDoctorListBinding
    lateinit var prediction: PreductionResult
    var doctorList = DoctorData.doctorList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoctorListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        subscribeUi()
        variableInitialization()
        subscribeClickListner()
    }

    private fun variableInitialization() {
        prediction = intent.getSerializableExtra("result") as PreductionResult
    }

    private fun subscribeClickListner() {
        val intent = Intent(this, UserChatActivity::class.java)
        intent.putExtra("result", prediction)
        intent.putExtra("from", "doctor")

        binding.dr1.setOnClickListener {
            intent.putExtra("name", doctorList[0].name)
            intent.putExtra("uid", doctorList[0].uid)
            startActivity(intent)
        }

        binding.dr2.setOnClickListener {
            intent.putExtra("name", doctorList[1].name)
            intent.putExtra("uid", doctorList[1].uid)
            startActivity(intent)
        }

        binding.dr3.setOnClickListener {
            intent.putExtra("name", doctorList[2].name)
            intent.putExtra("uid", doctorList[2].uid)
            startActivity(intent)
        }
    }

    private fun subscribeUi() {
    }
}