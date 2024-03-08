package com.example.sickpredict.repository

import com.example.sickpredict.data.Message.Message
import com.example.sickpredict.utils.ConstUtils.message
import com.example.sickpredict.utils.FirebaseUtils
import com.example.sickpredict.utils.FirebaseUtils.chatRef
import com.example.sickpredict.utils.FirebaseUtils.firebaseAuth
import com.example.sickpredict.utils.FirebaseUtils.firebaseDatabase
import com.google.firebase.auth.UserInfo

class Repository {





    /*
    send message
     */
    fun sendMessages(
        senderRoom: String,
        reciverRoom: String,
        message: Message,
        randomkey: String,
        recever_fcm_token: String
    ) {
        firebaseDatabase.reference
            .child("chats")
            .child(senderRoom)
            .child("message")
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
                    .child(reciverRoom)
                    .child("message")
                    .child(randomkey)
                    .setValue(message)
                    .addOnSuccessListener {

                    }

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


    /*
    get user info
     */
//    fun updateUserInfo(name: String, about: String, userId: String) {
//        val updates = mapOf<String, Any>(
//            "name" to name,
//            "about" to about
//        )
//        userRef.child(userId).updateChildren(updates)
//
//    }


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