package com.dicoding.asclepius.view.result

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.main.MainActivity.Companion.IMAGE_URI
import com.dicoding.asclepius.view.main.MainActivity.Companion.LABEL_LIST
import com.dicoding.asclepius.view.main.MainActivity.Companion.SCORE_LIST
import kotlin.math.round

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = Uri.parse(intent.getStringExtra(IMAGE_URI))
        val labelList = intent.getStringArrayListExtra(LABEL_LIST)
        val scoreList = intent.getStringArrayListExtra(SCORE_LIST)

        val highScoreRounded = rounded((scoreList?.get(0)?.toFloat() ?: 0f))
        val lowScoreRounded = rounded((scoreList?.get(1)?.toFloat() ?: 0f))
        val scorePercentage = getString(R.string.score_percentage)
        val highScorePercentage = String.format(scorePercentage, highScoreRounded)
        val lowScorePercentage = String.format(scorePercentage, lowScoreRounded)

        binding.resultImage.setImageURI(imageUriString)
        binding.resultText.text = labelList?.get(0)
        binding.tvHighScoreLabel.text = labelList?.get(0)
        binding.tvLowScoreLabel.text = labelList?.get(1)
        binding.tvHighScoreValue.text = highScorePercentage
        binding.tvLowScoreValue.text = lowScorePercentage
        binding.lpiHighScore.progress = highScoreRounded
        binding.lpiLowScore.progress = lowScoreRounded

    }

    private fun rounded(score: Float): Int {
        val roundedScore = round(score * 100)
        return roundedScore.toInt()
    }
}