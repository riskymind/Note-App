package com.asterisk.noteapp.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.asterisk.noteapp.R
import com.asterisk.noteapp.databinding.FragmentAddEditNoteBinding
import com.asterisk.noteapp.databinding.FragmentLoginBinding
import com.asterisk.noteapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        subscribeToLoginState()

        binding.btnLoginAccount.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            viewModel.loginUser(email, password)

        }

        binding.tvRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment()
            findNavController().navigate(action)
        }

    }


    private fun subscribeToLoginState() = lifecycleScope.launch {

        viewModel.loginState.collect { result ->
            when (result) {
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), result.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    val action = LoginFragmentDirections.actionLoginFragmentToNotesFragment()
                    findNavController().navigate(action)
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }

}