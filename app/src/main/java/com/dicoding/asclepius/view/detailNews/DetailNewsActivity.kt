package com.dicoding.asclepius.view.detailNews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityDetailNewsBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(TITLE)
        val author = intent.getStringExtra(AUTHOR)
        val imageString = intent.getStringExtra(IMAGE)
        val sourceName = intent.getStringExtra(SOURCE_NAME)
        val publishedAt = intent.getStringExtra(PUBLISHED_AT)
        val description = intent.getStringExtra(DESCRIPTION)

        val cleanDescription = description?.let { cleanText(it) }
        val date = publishedAt?.let { getDate(it) }

        val sourceFormat = getString(R.string.source)

        if (imageString != "null") {
            val imageUri = imageString?.toUri()
            Glide.with(this)
                .load(imageUri)
                .into(binding.ivNews)
        }

        with(binding) {
            tvTitle.text = title
            tvAuthor.text = author
            tvPublishedAt.text = date
            tvSource.text = String.format(sourceFormat, sourceName)
            tvDesc.text = cleanDescription
        }
    }

    private fun cleanText(text: String): String {
        return text
            .replace("\n", "")
            .replace("\t", "")
            .replace("\r", "")
            .replace("\\n", "")
            .replace("\\r", "")
            .replace("\\t", "")
    }

    private fun getDate(dateAndTime: String): String {
        val instant = Instant.parse(dateAndTime)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
        val outputDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return localDateTime.format(outputDateFormat)
    }

    companion object {
        const val TITLE = "title"
        const val AUTHOR = "author"
        const val IMAGE = "image"
        const val SOURCE_NAME = "source_name"
        const val PUBLISHED_AT = "published_at"
        const val DESCRIPTION = "description"
    }
}