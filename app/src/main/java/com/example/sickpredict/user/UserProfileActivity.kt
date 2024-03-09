package com.example.sickpredict.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sickpredict.R
import com.example.sickpredict.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}