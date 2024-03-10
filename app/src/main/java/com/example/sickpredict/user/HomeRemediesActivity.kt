package com.example.sickpredict.user

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import com.example.sickpredict.databinding.ActivityHomeRemediesBinding
import com.example.sickpredict.utils.DialogUtils
import com.google.ai.client.generativeai.GenerativeModel
import com.mukesh.MarkDown
import kotlinx.coroutines.launch

class HomeRemediesActivity : AppCompatActivity() {

    private val apiKey = "AIzaSyCc8ixIwiL12bTOxFbRkTOJDGcm5m69Xyc"
    private lateinit var alertDialog: AlertDialog
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )
    lateinit var binding: ActivityHomeRemediesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeRemediesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        variableInitialization()

        subscribeonClickListener()
    }

    private fun variableInitialization() {
        alertDialog = DialogUtils.buildLoadingDialog(this)
    }

    private fun subscribeonClickListener() {
        binding.predictButton.setOnClickListener {
            val prompt = "I want you to suggest me some home remedies for ${binding.etProblem.text}"
            alertDialog.show()

            try {
                lifecycleScope.launch { // Using lifecycleScope from androidx.lifecycle:lifecycle-runtime-ktx
                    val response = generativeModel.generateContent(prompt)
                    binding.composeView.apply {
                        // Dispose of the Composition when the view's LifecycleOwner is destroyed
                        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                        setContent {
                            // In Compose world
                            MaterialTheme {
                                MarkDown(
                                    text = response.text.toString(),
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        binding.etProblem.text.clear()
                        alertDialog.dismiss()

                    }
                }
            } catch (e: Exception) {
                alertDialog.dismiss()
                Toast.makeText(this, "Some Things Want Wrong", Toast.LENGTH_SHORT).show()
            }

        }
    }
}