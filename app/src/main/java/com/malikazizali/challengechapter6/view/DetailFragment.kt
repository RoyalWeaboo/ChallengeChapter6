package com.malikazizali.challengechapter6.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    lateinit var binding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detailFragment_to_homeFragment)
        }

        getsetDetailData()
    }

    fun getsetDetailData(){
        val gambar = arguments?.getString("gambar")
        val judul = arguments?.getString("judul")
        val rating = arguments?.getString("rating")
        val tanggal = arguments?.getString("tanggal")
        val bahasa = arguments?.getString("bahasa")
        val detail = arguments?.getString("detail")

        Glide.with(requireActivity()).load("https://image.tmdb.org/t/p/w185$gambar").into(binding.movieImage)
        binding.movieTitle.text = judul
        binding.movieRating.text = rating
        binding.movieReleaseDate.text = tanggal
        binding.movieLang.text = bahasa
        binding.detaiLFilm.text = detail
    }
}