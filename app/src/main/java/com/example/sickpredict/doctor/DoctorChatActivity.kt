package com.example.sickpredict.doctor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.sickpredict.R
import com.example.sickpredict.adapter.MessageAdapter
import com.example.sickpredict.data.Message.Message
import com.example.sickpredict.databinding.ActivityDoctorChatBinding
import com.example.sickpredict.repository.ViewModel
import com.example.sickpredict.user.DoctorListActivity
import com.example.sickpredict.utils.DialogUtils
import com.example.sickpredict.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DoctorChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityDoctorChatBinding

    lateinit var alertDialog: AlertDialog
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var currentPhotoPath: String


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

        supportActionBar?.hide()

        // setting username and dp
        binding.personName.text = name
        Glide.with(this).load(img).placeholder(R.drawable.ic_baseline_person_24).into(binding.personImg)


        // fetching most recent message from firebase realtime database
        getMessagesFromFirebase()
    }

    private fun getMessagesFromFirebase() {
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

        binding.cameraImg.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_IMAGE_CAPTURE
                    )
                } else {
                    dispatchTakePictureIntent()
                }
            } else {
                dispatchTakePictureIntent()
            }
        }

        // updating new text to firebase realtime database
        binding.sendBtn.setOnClickListener {

            val textMeassage = binding.message.text.toString()
            sendMessage(textMeassage, "text")
        }

        binding.back.setOnClickListener {
            startActivity(Intent(this, DoctorListActivity::class.java))
        }
    }

    private fun sendMessage(message: String, type: String) {
        val senderName = auth.currentUser!!.phoneNumber.toString()
        val randomkey = database.reference.push().key!!

        val messageBody = Message(
            message = message, // Store image URL as message
            sendUid = senderUid,
            timeSTamp = Date().time,
            messageId = randomkey,
            senderName = senderName,
            type = type
        )

        binding.message.setText("")
        viewModel.sendMessages(
            senderRoom = senderRoom,
            reciverRoom = reciverRoom,
            randomkey = randomkey,
            message = messageBody,
            recever_fcm_token = recever_fcm_token,
            receverUid = senderUid
        )


    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: Exception) {
                    null
                }
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.sickpredict.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image captured and saved to fileUri specified in the Intent
            val imageFile = File(currentPhotoPath)
            val imageUri = Uri.fromFile(imageFile)

            uploadImage(imageUri)
        } else {
            // Image capture failed, advise user
        }
    }


    private fun uploadImage(imageUri: Uri) {

        alertDialog.show()
        val storageRef = FirebaseUtils.firebaseStorage.reference
        val imageRef = storageRef.child("images/${imageUri.lastPathSegment}")

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val imageUrl = task.result.toString()
                // Send the imageUrl as a message
                sendMessage(imageUrl, "img")
                alertDialog.dismiss()
            } else {
                // Handle errors
                alertDialog.dismiss()
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun variableInit() {

        alertDialog = DialogUtils.buildLoadingDialog(this)



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


//
//rules_version = '2';
//
//// Craft rules based on data in your Firestore database
//// allow write: if firestore.get(
////    /databases/(default)/documents/users/$(request.auth.uid)).data.isAdmin;
//service firebase.storage {
//    match /b/{bucket}/o {
//
//        // This rule allows anyone with your Storage bucket reference to view, edit,
//        // and delete all data in your Storage bucket. It is useful for getting
//        // started, but it is configured to expire after 30 days because it
//        // leaves your app open to attackers. At that time, all client
//        // requests to your Storage bucket will be denied.
//        //
//        // Make sure to write security rules for your app before that time, or else
//        // all client requests to your Storage bucket will be denied until you Update
//        // your rules
//        match /DisplayPics/{userId}/{allPaths=**} {
//            allow read, write: true;
//        }
//    }
//}