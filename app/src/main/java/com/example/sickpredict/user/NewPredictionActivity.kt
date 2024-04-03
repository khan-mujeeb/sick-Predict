package com.example.sickpredict.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sickpredict.R
import com.example.sickpredict.api.MedicineRequestBody
import com.example.sickpredict.api.PredictionRequestBody
import com.example.sickpredict.api.RetrofitClient
import com.example.sickpredict.data.prediction.PreductionResult
import com.example.sickpredict.utils.DialogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewPredictionActivity : AppCompatActivity() {

    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_prediction)

        alertDialog = DialogUtils.buildLoadingDialog(this)

        // Create a list of symptoms
        val symptomsList =
            intent.getStringArrayListExtra("symptoms")!!// Replace with actual symptoms

        // Create a request body with the symptoms list
        val requestBody = PredictionRequestBody(symptomsList)

        CoroutineScope(Dispatchers.IO).launch {
            runOnUiThread { alertDialog.show() }
            try {
                val callPrediction = RetrofitClient.apiService.getPrediction(requestBody)
                val responsePrediction = callPrediction.execute()
                if (responsePrediction.isSuccessful) {
                    val predictionResponse = responsePrediction.body()
                    runOnUiThread { alertDialog.dismiss() }

                    runOnUiThread { alertDialog.show() }
                    val medicineRequestBody = MedicineRequestBody(
                        predictionResponse?.prediction ?: "", 22, "Male"
                    )

                    println("Prediction: ${predictionResponse?.prediction}, Accuracy: ${predictionResponse?.accuracy}")

                    val callMedicinePrediction =
                        RetrofitClient.medicineApiService.getMedicinePrediction(medicineRequestBody)
                    val responsePrediction2 = callMedicinePrediction.execute()

                    if (responsePrediction2.isSuccessful) {
                        runOnUiThread { alertDialog.dismiss() }
                        val medicinePredictionResponse2 = responsePrediction2.body()
                        println("Medicine Prediction: ${medicinePredictionResponse2!!.predicted_drugs}")
                        val temp = PreductionResult(
                            getCurrentDateTime()!!,
                            predictionResponse?.prediction!!,
                            predictionResponse.accuracy,
                            symptomsList,
                            medicinePredictionResponse2!!.predicted_drugs
                        )

//                            System.out.println("harshad" + drugs );


//                            System.out.println("harshad" + drugs );
                        val intent =
                            Intent(this@NewPredictionActivity, PredictionResultActivity::class.java)
                        intent.putExtra("result", temp)
                        startActivity(intent)
                    } else {
                        runOnUiThread { alertDialog.dismiss() }

                        Log.d("harshad", "${responsePrediction.code()}")
                        runOnUiThread {
                            Toast.makeText(
                                this@NewPredictionActivity,
                                "Failed to get medicine prediction: ${responsePrediction.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } else {
                    runOnUiThread { alertDialog.dismiss() }
                    runOnUiThread {
                        Toast.makeText(
                            this@NewPredictionActivity,
                            "Failed to get prediction: ${responsePrediction.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    println("Failed to get prediction: ${responsePrediction.code()}")
                }
            } catch (e: Exception) {
                runOnUiThread { alertDialog.dismiss() }
                runOnUiThread {
                    Toast.makeText(
                        this@NewPredictionActivity, "Error: ${e.message}", Toast.LENGTH_SHORT
                    ).show()
                }
                println("Error: ${e.message}")
            }
        }
    }

    fun getCurrentDateTime(): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
