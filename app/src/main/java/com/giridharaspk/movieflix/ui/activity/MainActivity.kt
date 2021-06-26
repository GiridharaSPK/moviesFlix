package com.giridharaspk.movieflix.ui.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.giridharaspk.movieflix.R
import com.giridharaspk.movieflix.data.api.ApiResult
import com.giridharaspk.movieflix.data.model.MovieResult
import com.giridharaspk.movieflix.data.repo.MoviesRepository
import com.giridharaspk.movieflix.databinding.ActivityMainBinding
import com.giridharaspk.movieflix.ui.adapter.MoviesAdapter
import com.giridharaspk.movieflix.ui.viewmodel.MainViewModel
import com.giridharaspk.movieflix.ui.viewmodel.ViewModelProviderFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownServiceException


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    private lateinit var viewModel: MainViewModel
    private val context: Context = this@MainActivity
    private val posterMap = HashMap<Int, Bitmap>()
    private val backDropMap = HashMap<Int, Bitmap>()
    private var moviesAdapter: MoviesAdapter = MoviesAdapter(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setupViewModel()
        setAdapters()
        setListeners()
        setObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProviderFactory(
                MainViewModel(
                    app = application,
                    MoviesRepository,
                )
            )
        ).get(MainViewModel::class.java)

    }

    private fun setAdapters() {
        binding.rvMovies.adapter = moviesAdapter
        binding.rvMovies.layoutManager = LinearLayoutManager(context)
    }

    private fun setListeners() {

    }

    private fun setObservers() {
        viewModel.imagesList.observe(this, Observer {
            when (it) {
                is ApiResult.Loading -> {
                    Log.d(TAG, "Loading")
                    showProgress()
                }
                is ApiResult.Success -> {
                    Log.d(TAG, "Success")
                    //handle success response
                    it.data?.let { resp ->
                        Log.d(TAG, "resp - $resp")

                        val loadingList = ArrayList<MovieResult>()
                        resp.results?.let { rows ->
                            rows.forEach { data ->
//                                if (data == null || (data.title == null && data.description == null && data.imageHref == null)) {
                                //ignore incorrect data
//                                } else {
                                loadingList.add(data)
                                Log.d(TAG, "data")
//                                }
                            }
                        }
                        /*runBlocking {
                            for (i in loadingList) {
                                getBackdropBitmap(i)
                                getPosterBitmap(i)
                            }
                            Log.d(TAG, "in job")
                        }*/
                        Log.d(TAG, "in runblo")
                        Log.d(TAG, "loadingList $loadingList")
                        moviesAdapter.list = loadingList
                        moviesAdapter.notifyDataSetChanged()
//                        supportActionBar?.title = resp.title
                    }
                    hideProgress()
                }
                is ApiResult.Failure -> {
                    Log.d(TAG, "Failure")
                    hideProgress()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                is ApiResult.Error -> {
                    Log.d(TAG, "Error")
                    hideProgress()
                    it.message?.let { message ->
                        Log.e(TAG, "An error occurred $message")
                    }
                    it.t?.let {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "api error", it)
                    }
                }
                is ApiResult.NetworkError -> {
                    hideProgress()
                    Log.e(TAG, "Network error")
                    Toast.makeText(this, "Please Check your network", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun hideProgress() {

    }

    private fun showProgress() {

    }

    private suspend fun getPosterBitmap(res: MovieResult) {
        if (res.id == null)
            return
        try {
            var bm = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ic_launcher_foreground
            )
            lifecycleScope.async(Dispatchers.IO) {
                try {
                    val url = URL("https://image.tmdb.org/t/p/w342/${res.poster_path}")
                    bm = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                } catch (e: IOException) {
                    println("Error")
                } catch (e: UnknownServiceException) {
                    println("Error2")
                } catch (e: MalformedURLException) {
                    println("Error3")
                }
            }.await()
            posterMap[res.id] = bm as Bitmap
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            Log.e(TAG, "getting bitmap error", e)
        }
    }

    private suspend fun getBackdropBitmap(res: MovieResult) {
        if (res.id == null)
            return
        try {
            var bm = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ic_launcher_foreground
            )
            withContext(lifecycleScope.coroutineContext + Dispatchers.IO) {
                try {
                    val url = URL("https://image.tmdb.org/t/p/original/${res.backdrop_path}")
                    Log.i(TAG, url.toString())
                    bm = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                } catch (e: IOException) {
                    println("Error")
                } catch (e: UnknownServiceException) {
                    println("Error2")
                } catch (e: MalformedURLException) {
                    println("Error3")
                }
            }
            backDropMap[res.id] = bm as Bitmap
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            Log.e(TAG, "getting bitmap error", e)
        }
    }


    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}

