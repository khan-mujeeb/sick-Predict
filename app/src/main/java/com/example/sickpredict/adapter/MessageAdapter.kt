package com.example.sickpredict.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.sickpredict.R
import com.example.sickpredict.data.Message.Message
import com.example.sickpredict.databinding.ImageReceiveItemviewBinding
import com.example.sickpredict.databinding.ImageSentItemviewBinding
import com.example.sickpredict.databinding.PredictionItemviewBinding
import com.example.sickpredict.databinding.ReceverItemViewBinding
import com.example.sickpredict.databinding.SentItemViewBinding
import com.example.sickpredict.utils.FirebaseUtils

class MessageAdapter(
    var context: Context,
    var list: ArrayList<Message>,
    val senderRoom: String,
    val reciverRoom: String,
    val viewModel: ViewModel
) : RecyclerView.Adapter<ViewHolder>() {


    val ITEM_SENT = 1
    val PRESCRPTION_SENT = 3
    val IMAGE_SENT = 5
    val IMAGE_RECEIVE = 6
    val PRESCRPTION_RECEIVE = 4
    val ITEM_RECEIVE = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ITEM_SENT)
            SentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.sent_item_view, parent, false)
            )
        else if (viewType == PRESCRPTION_SENT)
            PredictionViewHolder(
                LayoutInflater.from(context).inflate(R.layout.prediction_itemview, parent, false)
            )
        else if (viewType == PRESCRPTION_RECEIVE)
            PredictionViewHolder(
                LayoutInflater.from(context).inflate(R.layout.prescrption_receive_itemview, parent, false)
            )
        else if(viewType == IMAGE_SENT) {
            ImageSentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.image_sent_itemview, parent, false)
            )
        }else if(viewType == IMAGE_RECEIVE) {
            ImageReceiveViewHolder(
                LayoutInflater.from(context).inflate(R.layout.image_receive_itemview, parent, false)
            )
        }
        else
            ReceiverViewHolder(
                LayoutInflater.from(context).inflate(R.layout.recever_item_view, parent, false)
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var message = list[position]
        val type = holder.itemViewType
        if (type == ITEM_SENT) {
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.textSend.text = message.message
        }
        else if (type == PRESCRPTION_SENT) {
            val viewHolder = holder as PredictionViewHolder
            viewHolder.binding.textSend.text = message.prediction
            viewHolder.binding.medicinesList.text = message.medcines.toString()
            viewHolder.binding.symtomsList.text = message.symtomps.toString()
        }else if (type == PRESCRPTION_RECEIVE) {
            val viewHolder = holder as PredictionViewHolder
            viewHolder.binding.textSend.text = message.prediction
            viewHolder.binding.medicinesList.text = message.medcines.toString()
            viewHolder.binding.symtomsList.text = message.symtomps.toString()
        }
        else if (type == IMAGE_SENT) {
            val viewHolder = holder as ImageSentViewHolder
            Glide.with(context).load(message.message).into(viewHolder.binding.imageView)
        }
        else if (type == IMAGE_RECEIVE) {
            val viewHolder = holder as ImageReceiveViewHolder
            Glide.with(context).load(message.message).into(viewHolder.binding.imageView)
        }

        else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.textReceive.text = message.message
        }



    }


    override fun getItemViewType(position: Int): Int {
        return if (FirebaseUtils.firebaseAuth.uid == list[position].sendUid)
            return if(list[position].type == "prescription"  ) PRESCRPTION_SENT else if (list[position].type == "img") IMAGE_SENT else ITEM_SENT
        else
            return if(list[position].type == "prescription"  ) PRESCRPTION_RECEIVE else if (list[position].type == "img") IMAGE_RECEIVE else ITEM_RECEIVE
    }

    inner class SentViewHolder(view: View) : ViewHolder(view) {
        var binding = SentItemViewBinding.bind(view)
    }

    inner class ReceiverViewHolder(view: View) : ViewHolder(view) {
        var binding = ReceverItemViewBinding.bind(view)
    }

    inner class ImageSentViewHolder(view: View) : ViewHolder(view) {
        var binding = ImageSentItemviewBinding.bind(view)
    }

    inner class ImageReceiveViewHolder(view: View) : ViewHolder(view) {
        var binding = ImageReceiveItemviewBinding.bind(view)
    }

    inner class PredictionViewHolder(view: View) : ViewHolder(view) {
        var binding = PredictionItemviewBinding.bind(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}