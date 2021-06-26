package com.giridharaspk.movieflix.data.model

data class MoviesResponse(
    val dates: Dates?,
    val page: Int?,
    val results: List<MovieResult>?,
    val total_pages: Int?,
    val total_results: Int?
)