package com.malikazizali.challengechapter6.view

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.databinding.FragmentSplashBinding
import com.malikazizali.challengechapter6.viewmodel.UserViewModel

class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding
    lateinit var userViewModel : UserViewModel
    var session : String = "false"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //throw RuntimeException("Test Crash") // Force a crash

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        userViewModel.dataUser.observe(requireActivity()) {
            session = it.session
        }

        Handler().postDelayed({
            if(session == "false")
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment)
            else if(session=="true")
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment)
            else
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment)
        }, 3000)
    }
}