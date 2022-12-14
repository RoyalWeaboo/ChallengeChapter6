package com.malikazizali.challengechapter6.network

import com.malikazizali.challengechapter6.model.FavoritesResponseItem
import com.malikazizali.challengechapter6.model.Result
import retrofit2.Call
import retrofit2.http.*

interface RestfulApiFavorites {
    @GET("favorites")
    fun getAllMovie() : Call<List<FavoritesResponseItem>>
    @POST("favorites")
    fun addNewMovie(@Body res : FavoritesResponseItem) : Call<FavoritesResponseItem>
    @DELETE("favorites/{id}")
    fun deleteMovie(@Path("id") id : Int) : Call<Int>
}