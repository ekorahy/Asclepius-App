package com.dicoding.asclepius.view.main

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.entity.history.History
import com.dicoding.asclepius.repository.HistoryRepository

class MainViewModel(application: Application): ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    private val _currentImageUri = MutableLiveData<Uri?>(null)
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    fun insert(history: History) {
        mHistoryRepository.insert(history)
    }

    fun updateCurrentImageUri(uri: Uri) {
        _currentImageUri.postValue(uri)
    }

}