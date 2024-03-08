package com.example.sickpredict.doctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.sickpredict.R
import com.example.sickpredict.adapter.ChatAdapter
import com.example.sickpredict.databinding.ActivityDoctorDashboard2Binding
import com.example.sickpredict.repository.ViewModel
import com.google.firebase.auth.FirebaseAuth

class DoctorDashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDoctorDashboard2Binding
    lateinit var viewModel: ViewModel
    lateinit var uid: String
    lateinit var alertDailog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoctorDashboard2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        variableInit()
        subscribeUi()

    }

    private fun subscribeUi() {
        getPatientList()
    }

    private fun getPatientList() {
        alertDailog.show()
        viewModel.getUserList(uid) { uid, userList ->
            alertDailog.dismiss()
            println("mujeeb" + userList)
            binding.rc.adapter = ChatAdapter(this, userList)
        }
    }

    private fun variableInit() {

        uid = FirebaseAuth.getInstance().uid!!
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        alertDailog = AlertDialog.Builder(this).create()
    }
}