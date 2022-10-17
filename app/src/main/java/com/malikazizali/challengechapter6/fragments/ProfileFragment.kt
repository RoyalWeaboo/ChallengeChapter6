package com.malikazizali.challengechapter6.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.databinding.FragmentProfileBinding
import com.malikazizali.challengechapter6.viewmodel.BlurViewModelFactory
import com.malikazizali.challengechapter6.viewmodel.ProfilePictureViewModel


class ProfileFragment : Fragment() {
    lateinit var binding : FragmentProfileBinding
    private val viewModel: ProfilePictureViewModel by viewModels { BlurViewModelFactory(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEdit.setOnClickListener {
            viewModel.applyBlur(1)
        }
    }

}