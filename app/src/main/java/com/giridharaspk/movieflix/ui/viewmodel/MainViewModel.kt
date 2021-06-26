package com.giridharaspk.movieflix.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.giridharaspk.movieflix.data.api.ApiResult
import com.giridharaspk.movieflix.data.model.MoviesResponse
import com.giridharaspk.movieflix.data.repo.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class MainViewModel(app: Application, val repo: MoviesRepository) : AndroidViewModel(app) {
    val TAG = "MainViewModel"

    val imagesList =
        MutableLiveData<ApiResult<MoviesResponse>>()

    init {
        getMovies()
    }

    private fun getMovies() = viewModelScope.launch(Dispatchers.IO) {
        imagesList.postValue(ApiResult.Loading())
        try {
            val response = repo.getMovies()
            imagesList.postValue(handleImagesResponse(response))
        } catch (e: UnknownHostException) {
            imagesList.postValue(ApiResult.NetworkError(e))
        } catch (e: IOException) {
            imagesList.postValue(ApiResult.NetworkError(e))
        } catch (e: Exception) {
            imagesList.postValue(ApiResult.Error(e))
        }
    }

    private fun handleImagesResponse(resp: Response<MoviesResponse>): ApiResult<MoviesResponse> {
        Log.d(TAG, "handleImagesResponse $resp")
        return if (resp.isSuccessful) {
            if (resp.body()?.results?.isNotEmpty() == true) {
                Log.d(TAG, resp.body()?.results.toString())
                ApiResult.Success(resp.body()!!)
            } else {
                ApiResult.Failure("No Data")
            }
        } else {
            ApiResult.Failure(resp.message())
        }
    }

}