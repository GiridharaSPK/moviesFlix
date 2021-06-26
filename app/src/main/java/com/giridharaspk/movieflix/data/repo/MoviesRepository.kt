package com.giridharaspk.movieflix.data.repo

import android.util.Log
import com.giridharaspk.movieflix.data.api.RetrofitInstance
import com.giridharaspk.movieflix.data.model.MoviesResponse
import retrofit2.Response


object MoviesRepository {
    val TAG = "MoviesRepository"
    suspend fun getMovies(): Response<MoviesResponse> {
        Log.d(TAG, "getImages REST Api call")
        return RetrofitInstance.api.getMoviesList("a07e22bc18f5cb106bfe4cc1f83ad8ed")
    }
}