package com.dicoding.asclepius.view.detailHistory

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityDetailHistoryBinding
import kotlin.math.round

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgSrc = Uri.parse(intent.getStringExtra(IMG_SRC))
        val highScoreLabel = intent.getStringExtra(HIGH_SCORE_LABEL)
        val highScoreValue = intent.getStringExtra(HIGH_SCORE_VALUE)
        val lowScoreLabel = intent.getStringExtra(LOW_SCORE_LABEL)
        val lowScoreValue = intent.getStringExtra(LOW_SCORE_VALUE)
        val date = intent.getStringExtra(DATE)

        val scorePercentage = getString(R.string.score_percentage)
        val highScorePercentage = String.format(scorePercentage, highScoreValue?.toInt())
        val lowScorePercentage = String.format(scorePercentage, lowScoreValue?.toInt())

        with(binding) {
            ivImage.setImageURI(imgSrc)
            tvResult.text = highScoreLabel
            lpiHighScore.progress = highScoreValue?.toInt() ?: 0
            tvHighScoreLabel.text = highScoreLabel
            tvHighScoreValue.text = highScorePercentage
            lpiLowScore.progress = lowScoreValue?.toInt() ?: 0
            tvLowScoreLabel.text = lowScoreLabel
            tvLowScoreValue.text = lowScorePercentage
            tvDate.text = date
        }
    }

    companion object {
        const val IMG_SRC = "img_src"
        const val HIGH_SCORE_LABEL = "high_score_label"
        const val HIGH_SCORE_VALUE = "high_score_value"
        const val LOW_SCORE_LABEL = "low_score_label"
        const val LOW_SCORE_VALUE = "low_score_value"
        const val DATE = "date"
    }
}