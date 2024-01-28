package com.dicoding.asclepius.view.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.response.NewsResponse
import com.dicoding.asclepius.data.remote.retrrofit.ApiConfig
import com.dicoding.asclepius.utils.StringResourceFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    private val _news = MutableLiveData<List<ArticlesItem>>()
    val news: LiveData<List<ArticlesItem>> = _news

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<StringResourceFormatter>()
    val toastText: LiveData<StringResourceFormatter> = _toastText

    init {
        getNews()
    }

    private fun getNews() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getNews(Q, SORT_BY, API_KEY)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _news.value = response.body()?.articles
                    }
                } else {
                    _toastText.value = StringResourceFormatter.StringResource(R.string.failed_to_load)
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = StringResourceFormatter.StringResource(R.string.connection_problem)
            }

        })
    }

    companion object {
        private const val Q = "Cancer"
        private const val SORT_BY = "popularity"
        private const val API_KEY = "f863d44a1eee46fc9fd996257490d351"
    }
}