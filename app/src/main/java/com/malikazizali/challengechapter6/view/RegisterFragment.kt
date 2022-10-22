package com.malikazizali.challengechapter6.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.databinding.FragmentRegisterBinding
import com.malikazizali.challengechapter6.viewmodel.UserViewModel

class RegisterFragment : Fragment() {
    lateinit var binding : FragmentRegisterBinding
    lateinit var userViewModel : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerProgressBar.visibility = View.GONE
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        val usernameInput = binding.name.editText?.text
        val namaLengkapInput = binding.namaLengkap.editText?.text
        val passwordInput = binding.password.editText?.text
        val conPassInput = binding.conPassword.editText?.text

        binding.btnRegister.setOnClickListener {
            binding.registerProgressBar.visibility = View.VISIBLE
            if (passwordInput.toString() == conPassInput.toString()) {
                userViewModel.editData(
                    usernameInput.toString(),
                    namaLengkapInput.toString(),
                    passwordInput.toString(),
                    "false"
                )
                binding.registerProgressBar.visibility = View.GONE
                Toast.makeText(
                    requireActivity(), context?.getString(R.string.success_reg), Toast.LENGTH_SHORT
                ).show()
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
            }else {
                Toast.makeText(
                    requireActivity(),
                    context?.getString(R.string.pass_tidak_sesuai),
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.registerProgressBar.visibility = View.GONE
            }
            binding.registerProgressBar.visibility = View.GONE
        }

        binding.tvLogin.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

}