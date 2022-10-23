package com.malikazizali.challengechapter6.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malikazizali.challengechapter6.model.FavoritesResponseItem
import com.malikazizali.challengechapter6.model.Result
import com.malikazizali.challengechapter6.network.RestfulApiFavorites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel@Inject constructor(var api : RestfulApiFavorites): ViewModel() {
    var liveDataFav: MutableLiveData<List<FavoritesResponseItem>>
    var postFav: MutableLiveData<FavoritesResponseItem>
    var delFav: MutableLiveData<Int>
    var loading = MutableLiveData<Boolean>()

    init {
        liveDataFav = MutableLiveData()
        postFav = MutableLiveData()
        delFav = MutableLiveData()
        callApiFilm()
    }

    fun getLDMovie(): MutableLiveData<List<FavoritesResponseItem>> {
        return liveDataFav
    }

    fun postFavMovie(): MutableLiveData<FavoritesResponseItem> {
        return postFav
    }

    fun delFavMovie(): MutableLiveData<Int> {
        return delFav
    }

    fun callApiFilm() {
        loading.postValue(true)
        api.getAllMovie()
            .enqueue(object : Callback<List<FavoritesResponseItem>> {
                override fun onResponse(
                    call: Call<List<FavoritesResponseItem>>,
                    response: Response<List<FavoritesResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        liveDataFav.postValue(response.body())
                        Log.d("data", response.body().toString())
                    } else {
                        Log.d("data", response.body().toString())
                    }
                    loading.postValue(false)
                }

                override fun onFailure(call: Call<List<FavoritesResponseItem>>, t: Throwable) {
                    Log.d("data", call.toString())
                    loading.postValue(false)
                }

            })
    }

    fun callPostFavMovie(posterPath : String, originalTitle : String, voteAverage : String, releaseDate : String, originalLanguage : String, overview : String) {
        api.addNewMovie(FavoritesResponseItem("",originalLanguage,originalTitle,posterPath,releaseDate,voteAverage,overview))
            .enqueue(object : Callback<FavoritesResponseItem> {
                override fun onResponse(
                    call: Call<FavoritesResponseItem>,
                    response: Response<FavoritesResponseItem>
                ) {
                    if (response.isSuccessful) {
                        postFav.postValue(response.body())
                    } else {
                        Log.d("data", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<FavoritesResponseItem>, t: Throwable) {
                    Log.d("data", call.toString())
                }

            })
    }

    fun callDeleteFavMovie(id: Int) {
            api.deleteMovie(id)
                .enqueue(object : Callback<Int> {
                    override fun onResponse(
                        call: Call<Int>,
                        response: Response<Int>
                    ) {
                        if (response.isSuccessful) {
                            delFav.postValue(response.body())
                        } else {
                            Log.d("data", response.body().toString())
                        }
                        callApiFilm()
                        loading.postValue(false)
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Log.d("data", call.toString())
                        callApiFilm()
                    }
                })
        }

}