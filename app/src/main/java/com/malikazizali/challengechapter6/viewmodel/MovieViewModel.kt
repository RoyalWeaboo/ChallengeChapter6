package com.malikazizali.challengechapter6.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malikazizali.challengechapter6.model.MovieResponse
import com.malikazizali.challengechapter6.network.RestfulApiMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(var api : RestfulApiMovie): ViewModel() {
    var liveDataMovie: MutableLiveData<MovieResponse>
    var loading = MutableLiveData<Boolean>()

    init {
        liveDataMovie = MutableLiveData()
        callApiFilm()
    }

    fun getLDMovie(): MutableLiveData<MovieResponse> {
        return liveDataMovie
    }

    fun callApiFilm() {
        loading.postValue(true)
        api.getAllMovie()
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        liveDataMovie.postValue(response.body())
                        Log.d("data", response.body()?.results.toString())
                    } else {
                        Log.d("data", response.body()?.results.toString())
                    }
                    loading.postValue(false)
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("data", call.toString())
                    loading.postValue(false)
                }

            })
    }

}