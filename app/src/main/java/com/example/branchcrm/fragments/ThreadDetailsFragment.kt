package com.example.branchcrm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.branchcrm.API_FAILED
import com.example.branchcrm.LIST_ITEMS
import com.example.branchcrm.adapters.ThreadDetailsAdapter
import com.example.branchcrm.adapters.showToast
import com.example.branchcrm.databinding.FragmentThreadDetailsBinding
import com.example.branchcrm.models.MessageModel
import com.example.branchcrm.network.NetworkHelper
import com.example.branchcrm.viewmodel.ThreadDetailsViewModel
import kotlinx.coroutines.flow.collectLatest

class ThreadDetailsFragment : Fragment() {

    private val binding : FragmentThreadDetailsBinding by lazy { FragmentThreadDetailsBinding.inflate(layoutInflater) }

    private val viewModel : ThreadDetailsViewModel by lazy { ViewModelProvider(this)[ThreadDetailsViewModel::class.java] }

    private lateinit var adapter : ThreadDetailsAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getParcelableArrayList<MessageModel?>(LIST_ITEMS)
        addObservables()

        with(binding){
            replyLayout.setEndIconOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                if (NetworkHelper(requireContext()).isNetworkConnected()){
                    if (reply.text.isNullOrEmpty().not()){
                        data?.get(0)?.let { it1 -> viewModel.sendMessages(it1.threadId,reply.text.toString()) }
                    }
                }else{
                    showToast("Please check your Internet Connection")
                    binding.progressBar.visibility = View.GONE
                }
            }

            backBTN.setOnClickListener {
                findNavController().popBackStack()
            }


            adapter = ThreadDetailsAdapter()
            binding.rvThreadDetails.adapter = adapter
            if (data?.size!! <=1){
                adapter.setSingleData(data[0])
            }else{
                adapter.setThreadItems(data as ArrayList<MessageModel>)
            }
            binding.rvThreadDetails.smoothScrollToPosition(adapter.itemCount - 1)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

    }
    private fun addObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.apiStatus.collectLatest { message ->
                when (message){
                    API_FAILED -> {
                        showToast("Oops something went wrong!")
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.message.collectLatest { sentMessage ->
                if (sentMessage != null) {
                    adapter.addOurMessage(sentMessage)
                    binding.reply.text?.clear()
                    binding.rvThreadDetails.smoothScrollToPosition(adapter.itemCount - 1)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

    }


}