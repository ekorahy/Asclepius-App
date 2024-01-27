package com.dicoding.asclepius.view.main

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.repository.HistoryRepository

class MainViewModel(application: Application): ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    fun insert(history: History) {
        mHistoryRepository.insert(history)
    }
}