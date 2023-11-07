package com.example.branchcrm.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.branchcrm.databinding.ItemThreadDetailsBinding
import com.example.branchcrm.models.MessageModel

class ThreadDetailsAdapter : RecyclerView.Adapter<ThreadDetailsAdapter.ViewHolder>() {

    private var thread = ArrayList<MessageModel>()

    inner class ViewHolder(private val binding: ItemThreadDetailsBinding): RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(item:MessageModel) = binding.apply {

            userId.text = item.userId
            body.text = item.body
            date.text = item.timestamp.formatDate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemThreadDetailsBinding.inflate(
        LayoutInflater.from(parent.context),parent,false))


    override fun getItemCount() = thread.size

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(thread[position])
        }

    fun setThreadItems(threadItems:ArrayList<MessageModel>){
        thread = threadItems.reversed() as ArrayList<MessageModel>
    }

    fun addOurMessage(mess:MessageModel){
        thread.add(mess)
        notifyItemInserted(thread.size - 1)
    }

    fun setSingleData(mess:MessageModel){
        thread.add(mess)
    }
}