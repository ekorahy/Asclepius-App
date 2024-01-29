package com.dicoding.asclepius.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.round

class DataFormatter {
    fun cleanText(text: String): String {
        return text
            .replace("\n", "")
            .replace("\t", "")
            .replace("\r", "")
            .replace("\\n", "")
            .replace("\\r", "")
            .replace("\\t", "")
    }

    fun getDate(dateAndTime: String): String {
        val instant = Instant.parse(dateAndTime)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
        val outputDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return localDateTime.format(outputDateFormat)
    }

    fun rounded(score: Float): Int {
        val roundedScore = round(score * 100)
        return roundedScore.toInt()
    }
}