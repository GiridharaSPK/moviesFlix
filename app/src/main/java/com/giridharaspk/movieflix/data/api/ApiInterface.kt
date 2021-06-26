package com.giridharaspk.movieflix.data.api

import com.giridharaspk.movieflix.data.model.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("3/movie/now_playing")
    suspend fun getMoviesList(@Query("api_key") apiKey: String): Response<MoviesResponse>
}