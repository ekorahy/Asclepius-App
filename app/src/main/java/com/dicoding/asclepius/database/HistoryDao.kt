package com.dicoding.asclepius.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: History)

    @Query("SELECT * from history ORDER BY id ASC")
    fun getAllHistory(): LiveData<List<History>>

    @Query("DELETE from history where id = :id")
    fun deleteById(id: Int)
}