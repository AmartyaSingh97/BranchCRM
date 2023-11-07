package com.example.branchcrm.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.branchcrm.API_FAILED
import com.example.branchcrm.LIST_ITEMS
import com.example.branchcrm.R
import com.example.branchcrm.adapters.ThreadListAdapter
import com.example.branchcrm.adapters.showToast
import com.example.branchcrm.databinding.FragmentChatListBinding
import com.example.branchcrm.models.MessageModel
import com.example.branchcrm.viewmodel.ThreadListViewModel
import kotlinx.coroutines.flow.collectLatest


class ThreadListFragment : Fragment() {

    private val binding : FragmentChatListBinding by lazy { FragmentChatListBinding.inflate(layoutInflater) }

    private val viewModel: ThreadListViewModel by lazy { ViewModelProvider(this)[ThreadListViewModel::class.java] }

    private lateinit var adapter : ThreadListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addObservables()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.threadListFragment){
                requireActivity().finish()
            }
        }

    binding.backBTN.setOnClickListener {
        AlertDialog.Builder(requireContext()).setTitle("Are you sure you want to logout?")
            .setMessage("Your data will be reset.")
            .setPositiveButton("Yes"){dialog,_ ->
                viewModel.reset()
                dialog.dismiss()
                requireActivity().finish()
            }
            .setNegativeButton("No"){dialog,_ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    }

    private fun addObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.apiStatus.collectLatest { message->
                when (message){
                    API_FAILED -> {
                        showToast("Oops something went wrong!")

                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.messages.collectLatest { messageFromApi ->
                messageFromApi?.let { message ->
                    val grouped = message.groupBy { it.threadId }
                    val firstMessageList = ArrayList<MessageModel>()
                    for(i in grouped.keys){
                        grouped[i]?.get(0)?.let { it1 -> firstMessageList.add(it1) }
                    }
                    adapter = ThreadListAdapter { threadId ->
                        val list = grouped[threadId]
                        val bundle = bundleOf(LIST_ITEMS to (list as ArrayList<out Parcelable>))
                        findNavController().navigate(R.id.action_threadListFragment_to_threadDetailsFragment, bundle)
                    }
                    binding.rvThreadList.adapter = adapter
                    adapter.setThreadItems(firstMessageList)

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMessages()
    }

}