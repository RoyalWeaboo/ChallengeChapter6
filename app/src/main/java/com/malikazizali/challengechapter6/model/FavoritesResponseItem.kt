package com.malikazizali.challengechapter6.model


import com.google.gson.annotations.SerializedName

data class FavoritesResponseItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("originalLanguage")
    val originalLanguage: String,
    @SerializedName("originalTitle")
    val originalTitle: String,
    @SerializedName("posterPath")
    val posterPath: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("voteAverage")
    val voteAverage: String,
    @SerializedName("overview")
    val overview: String
)