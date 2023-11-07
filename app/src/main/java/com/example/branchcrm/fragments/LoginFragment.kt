package com.example.branchcrm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.branchcrm.API_FAILED
import com.example.branchcrm.API_SUCCESS
import com.example.branchcrm.AUTH
import com.example.branchcrm.R
import com.example.branchcrm.adapters.showToast
import com.example.branchcrm.databinding.FragmentLoginBinding
import com.example.branchcrm.network.AppObjectController
import com.example.branchcrm.network.NetworkHelper
import com.example.branchcrm.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val binding : FragmentLoginBinding by lazy { FragmentLoginBinding.inflate(layoutInflater) }

    private val viewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            if (AppObjectController.read(requireContext(), AUTH).isNullOrEmpty().not()){
                findNavController().navigate(R.id.action_loginFragment_to_threadListFragment)
            }
        }

        setObservables()

        with(binding) {
            buttonLogin.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                if (NetworkHelper(requireContext()).isNetworkConnected()) {
                if (editTextEmail.text.isNullOrEmpty().not() && editTextPassword.text.isNullOrEmpty().not()) {
                    viewModel.login(editTextEmail.text.toString(), editTextPassword.text.toString())
                } else {
                    binding.progressBar.visibility = View.GONE
                    showToast("Please enter the required fields !!")
                    editTextEmail.error = "Email cannot be empty !!"
                    editTextPassword.requestFocus()
                    editTextPassword.error = "Password cannot be empty !!"
                }
            }
                else{
                    binding.progressBar.visibility = View.GONE
                    showToast("Please check your Internet Connection !!")
                }
            }
        }
    }

    private fun setObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.apiStatus.collectLatest { message->
                when(message){
                    API_SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_loginFragment_to_threadListFragment)
                    }
                    API_FAILED -> {
                        binding.progressBar.visibility = View.GONE
                        showToast("Oops something went wrong!")
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.authToken.collectLatest {
                lifecycleScope.launch {
                    if (it != null) {
                        AppObjectController.save(requireContext(), AUTH, it)
                    }
                }
            }
        }
    }



}