package com.asterisk.noteapp.ui.auth.register

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
import com.asterisk.noteapp.databinding.FragmentCreateAccountBinding
import com.asterisk.noteapp.databinding.FragmentLoginBinding
import com.asterisk.noteapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CreateAccountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateAccountBinding.bind(view)

        subscribeToRegisterEvent()

        binding.btnCreateAccount.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            viewModel.createUser(name, email, password)
        }

        binding.tvLogin.setOnClickListener {
            val action =
                CreateAccountFragmentDirections.actionCreateAccountFragmentToLoginFragment()
            findNavController().navigate(action)
        }

    }


    private fun subscribeToRegisterEvent() = lifecycleScope.launch(Dispatchers.Main) {
        viewModel.registerState.collect { result ->
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
                    val action =
                        CreateAccountFragmentDirections.actionCreateAccountFragmentToLoginFragment()
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