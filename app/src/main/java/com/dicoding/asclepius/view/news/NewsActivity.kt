package com.dicoding.asclepius.view.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.NewsResponse
import com.dicoding.asclepius.data.retrrofit.ApiConfig
import com.dicoding.asclepius.databinding.ActivityDetailHistoryBinding
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvArticles.layoutManager = layoutManager
        getNews()
    }

    private fun getNews() {
        val client = ApiConfig.getApiService().getNews(Q, SORT_BY, API_KEY)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setNewsData(responseBody.articles)
                    }
                } else {
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                showToast(t.message.toString())
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setNewsData(news: List<ArticlesItem>) {
        val adapter = NewsAdapter()
        adapter.submitList(news)
        binding.rvArticles.adapter = adapter
    }

    companion object {
        private const val Q = "Cancer"
        private const val SORT_BY = "popularity"
        private const val API_KEY = "f863d44a1eee46fc9fd996257490d351"
    }
}