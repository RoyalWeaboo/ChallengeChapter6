package com.malikazizali.challengechapter6.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.databinding.ItemMovieBinding
import com.malikazizali.challengechapter6.model.Result

class MovieAdapter (var listMovie : List<Result>): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    var onAddFavorites : ((Result)->Unit)? = null
    class ViewHolder(var binding : ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load("https://image.tmdb.org/t/p/w185"+listMovie[position].posterPath).into(holder.binding.moviePoster)
        holder.binding.movieTitle.text = listMovie[position].originalTitle
        holder.binding.movieLang.text = holder.itemView.context?.getString(R.string.bahasa)+" "+listMovie[position].originalLanguage
        holder.binding.movieReleaseDate.text = holder.itemView.context?.getString(R.string.tanggal_rilis)+" "+listMovie[position].releaseDate
        holder.binding.movieRating.text = listMovie[position].voteAverage.toString()

        holder.binding.cardMovie.setOnClickListener{
            val arg = Bundle()
            arg.putString("gambar", listMovie[position].posterPath)
            arg.putString("judul", listMovie[position].originalTitle)
            arg.putString("rating", listMovie[position].voteAverage.toString())
            arg.putString("tanggal", listMovie[position].releaseDate)
            arg.putString("bahasa", listMovie[position].originalLanguage)
            arg.putString("detail", listMovie[position].overview)

            Navigation.findNavController(holder.itemView).navigate(R.id.action_homeFragment_to_detailFragment,arg)
        }

        holder.binding.addFav.setOnClickListener {
            onAddFavorites?.invoke(listMovie[position])
        }

    }

    override fun getItemCount(): Int {
        return listMovie.size
    }
}