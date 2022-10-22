package com.malikazizali.challengechapter6.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.databinding.FragmentFavoritesBinding
import com.malikazizali.challengechapter6.view.adapter.FavoritesAdapter
import com.malikazizali.challengechapter6.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    lateinit var binding: FragmentFavoritesBinding
    lateinit var favViewModel : FavoritesViewModel
    lateinit var adapter : FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favViewModel = ViewModelProvider(requireActivity()).get(FavoritesViewModel::class.java)

        setViewModelToAdapter()

        binding.arrowBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_favoritesFragment_to_homeFragment)
        }

    }

    private fun setViewModelToAdapter() {
        favViewModel.getLDMovie().observe(viewLifecycleOwner, Observer {
            favViewModel.loading.observe(viewLifecycleOwner, Observer {
                when (it) {
                    true -> binding.homeProgressBar.visibility = View.VISIBLE
                    false -> binding.homeProgressBar.visibility = View.GONE
                }
            })

            if (it != null) {
                binding.rvFavorites.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL, false
                )

                adapter = FavoritesAdapter(it)
                binding.rvFavorites.adapter = adapter

            } else {
                Toast.makeText(requireActivity(), context?.getString(R.string.no_data), Toast.LENGTH_SHORT).show()
            }
        })
        favViewModel.callApiFilm()
    }

}