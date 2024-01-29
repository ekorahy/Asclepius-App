package com.dicoding.asclepius.view.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvArticles.layoutManager = layoutManager

        val newsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[NewsViewModel::class.java]
        newsViewModel.news.observe(this) { news ->
            setNewsData(news)
        }

        newsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        newsViewModel.toastText.observe(this) {
            Toast.makeText(this, it.asString(this), Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setNewsData(news: List<ArticlesItem>) {
        val adapter = NewsAdapter()
        adapter.submitList(news)
        binding.rvArticles.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}