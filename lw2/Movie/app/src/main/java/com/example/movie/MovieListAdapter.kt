package com.example.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.databinding.MovieCardBinding

class MovieCardViewHolder(view: View) : RecyclerView.ViewHolder(view)

class MovieAdapter() : RecyclerView.Adapter<MovieCardViewHolder>() {
    var movieCardList = listOf<MovieItem>()

    override fun getItemCount(): Int = movieCardList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieCardBinding.inflate(inflater, parent, false)

        return MovieCardViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MovieCardViewHolder, position: Int) {
        val itemBinding = MovieCardBinding.bind(holder.itemView)
        val resources = holder.itemView.resources
        val movieCard = movieCardList[position]

        itemBinding.movieTitle.text = movieCard.title
        itemBinding.movieRateValue.text = movieCard.rate.toString()
        itemBinding.movieDescription.text = movieCard.description
        itemBinding.movieImage.setBackgroundResource(
            R.drawable.movie_1
        )
        itemBinding.cardView.setOnClickListener(
            val intent = Intent(context, MovieActivity::class.java)
        )
    }
}