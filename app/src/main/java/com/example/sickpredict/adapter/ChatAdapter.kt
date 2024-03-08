package com.example.sickpredict.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sickpredict.R
import com.example.sickpredict.data.Message.UserInfo
import com.example.sickpredict.data.user.User
import com.example.sickpredict.databinding.ChatItemViewBinding
import com.example.sickpredict.doctor.DoctorChatActivity
import com.example.sickpredict.user.UserChatActivity
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
//        Glide.with(context).load(user.imgUri)
//            .thumbnail(Glide.with(context).load(R.drawable.spinner))
//            .into(holder.binding.userImg)
        holder.binding.userName.text = user.fullname

//        55



        holder.itemView.setOnClickListener {
            val intent = Intent(context, DoctorChatActivity::class.java)
            intent.putExtra("patientUid", user.uid)
            intent.putExtra("patientName",user.fullname)
            intent.putExtra("img",user.profile)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}

