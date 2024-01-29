package com.dicoding.asclepius.view.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.ViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private var _activityHistoryBinding: ActivityHistoryBinding? = null
    private val binding get() = _activityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityHistoryBinding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        adapter = HistoryAdapter()

        binding?.rvHistory?.layoutManager = LinearLayoutManager(this)
        binding?.rvHistory?.adapter = adapter

        val historyViewModel = obtainViewModel(this@HistoryActivity)
        historyViewModel.getAllHistory().observe(this) { historyList ->
            if (historyList.isNullOrEmpty()) {
                binding?.apply {
                    rvHistory.visibility = View.GONE
                    tvEmptyData.visibility = View.VISIBLE
                }
            } else {
                adapter.setListHistory(historyList)
            }
        }
        adapter.setHistoryViewModel(historyViewModel)

        binding?.btnBack?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HistoryViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityHistoryBinding = null
    }
}