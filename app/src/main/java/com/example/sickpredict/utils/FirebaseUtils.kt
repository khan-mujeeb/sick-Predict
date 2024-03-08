package com.example.sickpredict.utils

import com.example.sickpredict.utils.ConstUtils.chats
import com.example.sickpredict.utils.ConstUtils.contact
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object FirebaseUtils {

    // basic ref
    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseUser = firebaseAuth.currentUser
    var firebaseStorage = FirebaseStorage.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()


    val chatRef = firebaseDatabase.getReference(chats)
    val contactRef = firebaseDatabase.getReference(contact)



}