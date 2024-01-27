package com.dicoding.asclepius.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class History (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "imgSrc")
    var imgSrc: String? = null,

    @ColumnInfo(name = "highScoreLabel")
    var highScoreLabel: String? = null,

    @ColumnInfo(name = "highScoreValue")
    var highScoreValue: Int? = null,

    @ColumnInfo(name = "lowScoreLabel")
    var lowScoreLabel: String? = null,

    @ColumnInfo(name = "lowScoreValue")
    var lowScoreValue: Int? = null,

    @ColumnInfo(name = "date")
    var date: String? = null
): Parcelable