package com.dicoding.asclepius.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.history.History
import com.dicoding.asclepius.data.local.room.history.HistoryDao
import com.dicoding.asclepius.data.local.room.history.HistoryRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application) {
    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryRoomDatabase.getDatabase(application)
        mHistoryDao = db.historyDao()
    }

    fun getAllHistory(): LiveData<List<History>> = mHistoryDao.getAllHistory()

    fun insert(history: History) {
        executorService.execute{ mHistoryDao.insert(history) }
    }

    fun deleteById(id: Int) {
        executorService.execute { mHistoryDao.deleteById(id) }
    }
}