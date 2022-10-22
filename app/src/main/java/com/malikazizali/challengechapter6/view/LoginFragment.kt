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
import com.malikazizali.challengechapter6.databinding.FragmentLoginBinding
import com.malikazizali.challengechapter6.databinding.FragmentRegisterBinding
import com.malikazizali.challengechapter6.viewmodel.UserViewModel

class LoginFragment : Fragment() {
    lateinit var binding : FragmentLoginBinding
    lateinit var userViewModel : UserViewModel
    lateinit var username : String
    lateinit var password : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginProgressBar.visibility = View.GONE
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        userViewModel.dataUser.observe(requireActivity()) {
            username = it.username
            password = it.password
        }

        binding.btnLogin.setOnClickListener {
            binding.loginProgressBar.visibility = View.VISIBLE
            val usernameInput = binding.etUsername.text.toString()
            val passwordInput = binding.etPassword.text.toString()
            if (usernameInput!=""&&passwordInput!="") {
                if(usernameInput == username && passwordInput == password){
                    userViewModel.editSession("true")
                    Toast.makeText(requireActivity(), context?.getString(R.string.success_login), Toast.LENGTH_SHORT).show()
                    binding.loginProgressBar.visibility = View.GONE
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
                }else{
                    binding.loginProgressBar.visibility = View.GONE
                    Toast.makeText(requireActivity(), context?.getString(R.string.failed_login), Toast.LENGTH_SHORT).show()
                }
            }
            else{
                binding.loginProgressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), context?.getString(R.string.empty_login_input), Toast.LENGTH_SHORT).show()
            }
            binding.loginProgressBar.visibility = View.GONE
        }

        binding.btnRegister.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }

}