package com.example.sickpredict.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sickpredict.adapter.MessageAdapter
import com.example.sickpredict.data.Message.Message
import com.example.sickpredict.data.prediction.PreductionResult
import com.example.sickpredict.data.user.User
import com.example.sickpredict.databinding.ActivityUserChatBinding
import com.example.sickpredict.repository.ViewModel
import com.example.sickpredict.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class UserChatActivity : AppCompatActivity() {

    var from = ""
    lateinit var binding: ActivityUserChatBinding

    // firebase variable's
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    // uid's
    private lateinit var senderUid: String
    private lateinit var reciverUid: String
    private lateinit var senderRoom: String
    private lateinit var reciverRoom: String

    // user data variable
    private lateinit var list: ArrayList<Message>
    private lateinit var img: String
    private lateinit var name: String
    lateinit var viewModel: ViewModel
    lateinit var recever_fcm_token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeOnClickEvents()
        variableInit()
        subscribeUi()

        if (from == "doctor") {
            val prediction = intent.getSerializableExtra("result") as? PreductionResult
            prediction?.let {
                addMedicalHstory(it)

                val predictionMsg = Message(
                    message = "",
                    senderUid,
                    Date().time,
                    database.reference.push().key!!,
                    "0000000000000000",
                    "prescription",
                    prediction = it.prediction,
                    accuracy = it.accuracy,
                    symtomps = it.symtomps.toString(),
                    medcines = it.medcines.toString()

                )
                viewModel.sendMessages(
                    senderRoom = senderRoom,
                    reciverRoom = reciverRoom,
                    randomkey = database.reference.push().key!!,
                    message = predictionMsg,
                    recever_fcm_token = recever_fcm_token,
                    receverUid = reciverUid
                )

            }
        }

    }

    private fun addMedicalHstory(prediction: PreductionResult) {
        viewModel.addPatientRecord(
            doctorUid = reciverUid,
            patientUid = senderUid,
            patientRecord = prediction)
    }


    private fun subscribeUi() {

        supportActionBar?.hide()

        // setting username and dp
        binding.personName.text = name
//        Glide.with(this).load(img).into(binding.personImg)


        // fetching most recent message from firebase realtime database
        FirebaseUtils.firebaseDatabase.reference
            .child("chats")
            .child(reciverUid)
            .child("messages")
            .child(senderRoom)

            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(Message::class.java)
                        list.add(data!!)
                    }


                    binding.messageRc.adapter = MessageAdapter(
                        this@UserChatActivity,
                        list,
                        senderRoom,
                        reciverRoom,
                        viewModel
                    )
                    binding.messageRc.scrollToPosition(list.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun subscribeOnClickEvents() {
        // updating new text to firebase realtime database
        binding.sendBtn.setOnClickListener {

            val senderName = auth.currentUser!!.phoneNumber.toString()
            val randomkey = database.reference.push().key!!

            val textMeassage = binding.message.text

            // creating message
            val message = Message(
                message = textMeassage.toString(),
                sendUid = senderUid,
                timeSTamp = Date().time,
                messageId = randomkey,
                senderName = senderName
            )

            FirebaseUtils.firebaseDatabase.getReference("Registered Users").child(senderUid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(User::class.java)
                            viewModel.addUserToPatientList(user!!, reciverUid)

                        } else {

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@UserChatActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                })


//            sending message
            if (textMeassage.isNotEmpty()) {
                binding.message.text = null
                viewModel.sendMessages(
                    senderRoom = senderRoom,
                    reciverRoom = reciverRoom,
                    randomkey = randomkey,
                    message = message,
                    recever_fcm_token = recever_fcm_token,
                    receverUid = reciverUid
                )
            } else {
                Toast.makeText(this, "Enter your text", Toast.LENGTH_SHORT).show()
            }
        }

        binding.back.setOnClickListener {
            startActivity(Intent(this, DoctorListActivity::class.java))
        }
    }

    private fun variableInit() {
        from = intent.getStringExtra("from")!!





        recever_fcm_token = "fegtrhthy"
        auth = FirebaseUtils.firebaseAuth
        database = FirebaseUtils.firebaseDatabase

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        senderUid = auth.uid.toString()
        reciverUid = intent.getStringExtra("uid")!!
        senderRoom = senderUid + reciverUid
        reciverRoom = reciverUid + senderUid

//        img = intent.getStringExtra("img")!!
        name = intent.getStringExtra("name")!!
        list = ArrayList()


    }





}