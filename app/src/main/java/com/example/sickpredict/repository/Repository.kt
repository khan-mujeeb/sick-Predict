package com.example.sickpredict.repository

import com.example.sickpredict.data.Message.Message
import com.example.sickpredict.data.Message.UserInfo
import com.example.sickpredict.data.prediction.PreductionResult
import com.example.sickpredict.data.user.User
import com.example.sickpredict.utils.ConstUtils.message
import com.example.sickpredict.utils.FirebaseUtils
import com.example.sickpredict.utils.FirebaseUtils.chatRef
import com.example.sickpredict.utils.FirebaseUtils.firebaseAuth
import com.example.sickpredict.utils.FirebaseUtils.firebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Repository {


    /*
    send message
     */
    fun sendMessages(
        senderRoom: String,
        reciverRoom: String,
        message: Message,
        randomkey: String,
        recever_fcm_token: String,
        receverUid: String
    ) {
        firebaseDatabase.reference
            .child("chats")
            .child(receverUid)
            .child("messages")

            .child(senderRoom)

            .child(randomkey)
            .setValue(message)
            .addOnSuccessListener {

                firebaseDatabase.reference
                    .child("chats")
                    .child(receverUid)
                    .child("messages")

                    .child(reciverRoom)

                    .child(randomkey)
                    .setValue(message)
                    .addOnSuccessListener {

                    }

            }
    }


    /*
     add user to patient list
     */
    fun addUserToPatientList(
        user: User,
        receverUid: String,

        ) {
        firebaseDatabase.reference
            .child("chats")
            .child(receverUid)
            .child("patientList")
            .child(user.uid)
            .setValue(user)
            .addOnSuccessListener {

            }
    }



    // fun to check user is created or not
    fun getUserList(uid: String, callback: (List<User>) -> Unit) {
        val userList = mutableListOf<User>()


        firebaseDatabase.getReference("chats").child(uid).child("patientList")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for (user in snapshot.children) {
                        val temp = user.getValue(User::class.java)!!
                        userList.add(temp)
                    }

                    callback(userList)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }


    /*
    delete sender message
     */
    fun deleteSenderMessage(senderRoom: String, messageId: String) {
        chatRef.child(senderRoom).child(message).child(messageId).removeValue()
        println("sender $senderRoom")
    }

    /*
    delete reciver message
    */
    fun deleteReciverMessage(reciverRoom: String, messageId: String) {

        chatRef.child(reciverRoom).child(message).child(messageId).removeValue()
    }




    fun addPatientRecord(doctorUid: String, patientUid: String, patientRecord: PreductionResult) {
        firebaseDatabase.reference
            .child("chats")
            .child(doctorUid)
            .child("patientRecords")
            .child(patientUid)
            .child(firebaseDatabase.reference.push().key.toString())
            .setValue(patientRecord)
    }

    fun readPatientRecord(doctorUid: String, patientUid: String, callback: (List<PreductionResult>?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.reference.child("chats").child(doctorUid).child("patientRecords").child(patientUid)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val records = snapshot.children.mapNotNull { it.getValue(PreductionResult::class.java) }
                callback(records)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                callback(null)
            }
        })
    }

}