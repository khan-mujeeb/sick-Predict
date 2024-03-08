package com.example.sickpredict.repository

import com.example.sickpredict.data.Message.Message
import com.example.sickpredict.data.Message.UserInfo
import com.example.sickpredict.data.user.User
import com.example.sickpredict.utils.ConstUtils.message
import com.example.sickpredict.utils.FirebaseUtils
import com.example.sickpredict.utils.FirebaseUtils.chatRef
import com.example.sickpredict.utils.FirebaseUtils.firebaseAuth
import com.example.sickpredict.utils.FirebaseUtils.firebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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

//                val notification = PushNotification(
//                    data = NotificationModel(
//                        title = message.senderName,
//                        body = message.message
//                    ),
//                    to = recever_fcm_token
//                )


//                ApiUtlis.getInstance().sendNotification(notification).enqueue(object : retrofit2.Callback<PushNotification>{
//                    override fun onResponse(
//                        call: Call<PushNotification>,
//                        response: Response<PushNotification>
//                    ) {
//
//                    }
//
//                    override fun onFailure(call: Call<PushNotification>, t: Throwable) {
//                        println("error khan ${t.message}")
//                    }
//
//                })

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

    /*
    get fcm tokken
     */
//    fun getFcmToken(receverId: String, callback: (String) -> Unit) {
//        userRef.child(receverId).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    val user = snapshot.getValue(UserInfo::class.java)
//                    callback(user!!.fcm_token)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }


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

    /*
    add contact
     */
    fun addContact(user: UserInfo) {
        val randomkey = firebaseDatabase.reference.push().key!!
        FirebaseUtils.contactRef.child(firebaseAuth.currentUser?.phoneNumber.toString())
            .child(randomkey)
            .setValue(user)
    }

    /*
    upload data
     */
//    fun uploadFileToStorage(uri: Uri, callback: (String?) -> Unit) {
//        val reference = storageRef.child(Date().time.toString())
//        reference.putFile(uri).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                reference.downloadUrl.addOnSuccessListener { uri ->
//                    callback(uri.toString())
//                }
//            } else {
//                callback(null)
//            }
//        }
//    }


}