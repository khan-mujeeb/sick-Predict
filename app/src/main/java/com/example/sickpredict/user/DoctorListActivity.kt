package com.example.sickpredict.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sickpredict.data.prediction.PreductionResult
import com.example.sickpredict.databinding.ActivityDoctorListBinding
import com.example.sickpredict.utils.DoctorData
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.util.Locale

class DoctorListActivity : AppCompatActivity(), PaymentResultListener {

    lateinit var binding: ActivityDoctorListBinding
    lateinit var prediction: PreductionResult
    var doctorList = DoctorData.doctorList
    var doctorId = 0
    var tts: TextToSpeech? = null
    lateinit var sharedPref: SharedPreferences
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoctorListBinding.inflate(layoutInflater)
        setContentView(binding.root)




        subscribeUi()
        variableInitialization()
        subscribeClickListner()
    }


    private fun makepayment() {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_dxS5keJI1OHqq2")


        val activity: Activity = this

        try {
            val options = JSONObject()
            options.put("name", "ALGORIAL EDCUARE")
            options.put("description", "Reference No. #123456")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", "30000") //300 X 100
            options.put("prefill.email", "gaurav.kumar@example.com")
            options.put("prefill.contact", "7864945278")
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e)
        }
    }

    private fun variableInitialization() {
        sharedPref = applicationContext.getSharedPreferences("my_prefs", MODE_PRIVATE)

        prediction = intent.getSerializableExtra("result") as PreductionResult
        name = sharedPref.getString("name", "") !!

        Log.d("MY NAME IS ", " $name")

    }





    private fun subscribeClickListner() {


        binding.dr1.setOnClickListener {
            doctorId = 1
            makepayment()
        }
        binding.dr2.setOnClickListener {
            doctorId = 2
            makepayment()
        }
        binding.dr3.setOnClickListener {
            doctorId = 3
            makepayment()
        }


    }

    private fun subscribeUi() {
        tts = TextToSpeech(applicationContext) { i ->
            if (i == TextToSpeech.SUCCESS) {
                val textToSpeak = "Welcome, $name The Predicted Disease is ${prediction.prediction} for entered symptoms are ${prediction.symtomps}. Please select a doctor to chat with."
                tts!!.language = Locale.US
                tts!!.setSpeechRate(0.5f)
                tts!!.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null)
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success$", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, UserChatActivity::class.java)
        intent.putExtra("result", prediction)
        intent.putExtra("from", "doctor")

        if(doctorId == 1) {
            intent.putExtra("name", doctorList[0].name)
            intent.putExtra("uid", doctorList[0].uid)
            startActivity(intent)
        } else if(doctorId == 2) {
            intent.putExtra("name", doctorList[1].name)
            intent.putExtra("uid", doctorList[1].uid)
            startActivity(intent)
        } else if(doctorId == 3) {
            intent.putExtra("name", doctorList[2].name)
            intent.putExtra("uid", doctorList[2].uid)
            startActivity(intent)
        }

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
    }
}