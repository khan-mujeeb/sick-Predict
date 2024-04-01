package com.example.sickpredict.doctor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.sickpredict.R
import com.example.sickpredict.adapter.MessageAdapter
import com.example.sickpredict.data.Message.Message
import com.example.sickpredict.databinding.ActivityDoctorChatBinding
import com.example.sickpredict.repository.ViewModel
import com.example.sickpredict.user.DoctorListActivity
import com.example.sickpredict.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class DoctorChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityDoctorChatBinding


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

        binding = ActivityDoctorChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeOnClickEvents()
        variableInit()
        subscribeUi()
    }



    private fun subscribeUi() {
        // setting username and dp
        binding.personName.text = name
        Glide.with(this).load(img).placeholder(R.drawable.ic_baseline_person_24).into(binding.personImg)


        // fetching most recent message from firebase realtime database
        FirebaseUtils.firebaseDatabase.reference
            .child("chats")
            .child(senderUid)
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
                        this@DoctorChatActivity,
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

//            sending message
            if (textMeassage.isNotEmpty()) {
                binding.message.text = null
                viewModel.sendMessages(
                    senderRoom = senderRoom,
                    reciverRoom = reciverRoom,
                    randomkey = randomkey,
                    message = message,
                    recever_fcm_token = recever_fcm_token,
                    receverUid = senderUid
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



        recever_fcm_token = "fegtrhthy"
        auth = FirebaseUtils.firebaseAuth
        database = FirebaseUtils.firebaseDatabase

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        senderUid = auth.uid.toString()
        reciverUid = intent.getStringExtra("patientUid")!!
        senderRoom = senderUid + reciverUid
        reciverRoom = reciverUid + senderUid

        img = intent.getStringExtra("patientProfile")!!
        name = intent.getStringExtra("patientName")!!
        list = ArrayList()


    }


}