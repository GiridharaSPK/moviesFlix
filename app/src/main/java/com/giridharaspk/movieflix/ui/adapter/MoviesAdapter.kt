package com.giridharaspk.movieflix.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.giridharaspk.movieflix.R
import com.giridharaspk.movieflix.data.model.MovieResult
import com.giridharaspk.movieflix.databinding.LayoutPopularMovieBinding
import com.giridharaspk.movieflix.databinding.LayoutUnpopularMovieBinding


class MoviesAdapter(val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TAG = "MoviesAdapter"
    var list: ArrayList<MovieResult> = ArrayList()
    var backdropImages: HashMap<Int, Bitmap?> = HashMap()
    var posters: HashMap<Int, Bitmap?> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i(TAG, "onCreateViewHolder ")
        return if (viewType == 0) {
            PopularMovieViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_popular_movie,
                    parent,
                    false
                )
            )
        } else {
            UnpopularMovieViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_unpopular_movie,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        Log.i(TAG, "getItemViewType ")
        return try {
            if (list[position].vote_count!! > 700) {
                0
            } else {
                1
            }
        } catch (e: Exception) {
            Log.e(TAG, "cannot get viewType ", e)
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieItem = list[position]
        if (holder is PopularMovieViewHolder) {
//            holder.binding.ivBackDrop.setImageBitmap(backdropImages[list[position].id])
            loadImage(holder.binding.ivBackDrop, "https://image.tmdb.org/t/p/original/${movieItem.backdrop_path}")
        } else if (holder is UnpopularMovieViewHolder) {
            holder.binding.apply {
                tvTitle.text = movieItem.title
                tvOverView.text = movieItem.overview
//                holder.binding.ivPoster.setImageBitmap(posters[list[position].id])
                loadImage(holder.binding.ivPoster, "https://image.tmdb.org/t/p/w342/${movieItem.backdrop_path}")
            }
        }
    }

    override fun getItemCount() = list.size

    private fun loadImage(imageView: ImageView, imageReference: String?) {
        Glide.with(context)
            .load(imageReference)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .override(200,200)
//            .placeholder(R.drawable.ic_launcher_foreground)
//            .error(R.drawable.ic_launcher_background)
            .into(imageView)
    }

}

internal class PopularMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: LayoutPopularMovieBinding = LayoutPopularMovieBinding.bind(itemView);
}

internal class UnpopularMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: LayoutUnpopularMovieBinding = LayoutUnpopularMovieBinding.bind(itemView)
}