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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.databinding.FragmentHomeBinding
import com.malikazizali.challengechapter6.model.SavedPreference
import com.malikazizali.challengechapter6.view.adapter.MovieAdapter
import com.malikazizali.challengechapter6.viewmodel.FavoritesViewModel
import com.malikazizali.challengechapter6.viewmodel.MovieViewModel
import com.malikazizali.challengechapter6.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: MovieAdapter
    lateinit var userViewModel : UserViewModel
    lateinit var namaLengkap : String
    lateinit var session : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(GoogleSignIn.getLastSignedInAccount(requireActivity())!=null){
            getGoogleData()
        }else{
            getUserData()
        }

        setViewModelToAdapter()

        binding.ivProfile.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.ivFavorite.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_favoritesFragment)
        }

    }

    private fun getGoogleData(){
        namaLengkap = SavedPreference.getUsername(requireActivity())!!
        binding.greetingText.text = namaLengkap
    }

    private fun getUserData(){
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        userViewModel.dataUser.observe(requireActivity()) {
            namaLengkap = it.namaLengkap
            session = it.session
        }
        binding.greetingText.text = namaLengkap
    }

    private fun setViewModelToAdapter() {
        val viewModel = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)

        viewModel.getLDMovie().observe(viewLifecycleOwner, Observer {
            viewModel.loading.observe(viewLifecycleOwner, Observer {
                when (it) {
                    true -> binding.homeProgressBar.visibility = View.VISIBLE
                    false -> binding.homeProgressBar.visibility = View.GONE
                }
            })

            if (it != null) {
                binding.rvMovie.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL, false
                )

                adapter = MovieAdapter(it.results)
                binding.rvMovie.adapter = adapter

                adapter.onAddFavorites={
//                    Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
                    binding.homeProgressBar.visibility = View.VISIBLE
                    val favViewModel = ViewModelProvider(requireActivity()).get(FavoritesViewModel::class.java)
                    favViewModel.callPostFavMovie(it.posterPath, it.originalTitle, it.voteAverage.toString(), it.releaseDate, it.originalLanguage, it.overview)
                    favViewModel.postFavMovie().observe(viewLifecycleOwner, Observer {
                        if(it != null){
                            binding.homeProgressBar.visibility = View.GONE
                            Toast.makeText(requireActivity(), context?.getString(R.string.tambah_film_fav), Toast.LENGTH_SHORT).show()
                        }
                        binding.homeProgressBar.visibility = View.GONE
                    })
                }

            } else {
                Toast.makeText(requireActivity(), context?.getString(R.string.no_data), Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.callApiFilm()
    }

}