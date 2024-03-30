package com.example.sickpredict.adapter


import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sickpredict.R
import com.example.sickpredict.data.Message.Message
import com.example.sickpredict.databinding.PredictionItemviewBinding
import com.example.sickpredict.databinding.ReceverItemViewBinding
import com.example.sickpredict.databinding.SentItemViewBinding
import com.example.sickpredict.utils.ConstUtils.cancel
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
        }

        else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.textReceive.text = message.message
        }



    }


    override fun getItemViewType(position: Int): Int {
        return if (FirebaseUtils.firebaseAuth.uid == list[position].sendUid)
            return if(list[position].type == "prescription"  ) PRESCRPTION_SENT else ITEM_SENT
        else
            ITEM_RECEIVE
    }

    inner class SentViewHolder(view: View) : ViewHolder(view) {
        var binding = SentItemViewBinding.bind(view)
    }

    inner class ReceiverViewHolder(view: View) : ViewHolder(view) {
        var binding = ReceverItemViewBinding.bind(view)
    }

    inner class PredictionViewHolder(view: View) : ViewHolder(view) {
        var binding = PredictionItemviewBinding.bind(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}