package com.example.sickpredict.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sickpredict.R
import com.example.sickpredict.data.Message.UserInfo
import com.example.sickpredict.data.user.User
import com.example.sickpredict.databinding.ChatItemViewBinding
import com.example.sickpredict.doctor.DoctorChatActivity
import com.example.sickpredict.user.PatientProfileActivity
import com.example.sickpredict.user.UserChatActivity
import com.example.sickpredict.utils.FirebaseUtils.firebaseDatabase
import com.google.firebase.database.FirebaseDatabase

class ChatAdapter(var context: Context,var list: List<User>):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    lateinit var database: FirebaseDatabase
    class ChatViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: ChatItemViewBinding = ChatItemViewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = (LayoutInflater.from(parent.context)).inflate(R.layout.chat_item_view,parent,false)
        return ChatViewHolder(view)
    }



    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var lastMessage = ""
        var user = list[position]



        firebaseDatabase.getReference("Registered Users").child(user.uid).child("profile").get().addOnSuccessListener {
            Log.d("TAG", "onBindViewHolder: ${it.value}")
            Glide.with(context).load(it.value.toString())
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.binding.userImg)

            user.profile = it.value.toString()
        }.addOnFailureListener {
            Log.d("TAG", "onBindViewHolder: ${it.message}")
        }



        holder.binding.userName.text = user.fullname


        holder.itemView.setOnClickListener {

            val intent = Intent(context, PatientProfileActivity::class.java)
            intent.putExtra("user", user)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}

