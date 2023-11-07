package com.example.branchcrm.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.branchcrm.databinding.ItemThreadListBinding
import com.example.branchcrm.models.MessageModel

class ThreadListAdapter (
     private var onItemClicked: ((clickPosition: Int) -> Unit)
) : RecyclerView.Adapter<ThreadListAdapter.ViewHolder>() {

    private var threads = ArrayList<MessageModel>()

    inner class ViewHolder(private val binding: ItemThreadListBinding):RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(item:MessageModel) = binding.apply {

            userId.text = item.userId
            body.text = item.body
            date.text = item.timestamp.formatDate()

            root.setOnClickListener {
                onItemClicked(item.threadId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemThreadListBinding.inflate(
        LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount() = threads.size

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(threads[position])
    }

    fun setThreadItems(threadItems:ArrayList<MessageModel>){
        threads = threadItems
    }

}