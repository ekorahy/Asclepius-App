package com.dicoding.asclepius.view.result

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.utils.DataFormatter
import com.dicoding.asclepius.view.main.MainActivity.Companion.IMAGE_URI
import com.dicoding.asclepius.view.main.MainActivity.Companion.LABEL_LIST
import com.dicoding.asclepius.view.main.MainActivity.Companion.SCORE_LIST

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = Uri.parse(intent.getStringExtra(IMAGE_URI))
        val labelList = intent.getStringArrayListExtra(LABEL_LIST)
        val scoreList = intent.getStringArrayListExtra(SCORE_LIST)

        val highScoreRounded = DataFormatter().rounded((scoreList?.get(0)?.toFloat() ?: 0f))
        val lowScoreRounded = DataFormatter().rounded((scoreList?.get(1)?.toFloat() ?: 0f))
        val scorePercentage = getString(R.string.score_percentage)
        val highScorePercentage = String.format(scorePercentage, highScoreRounded)
        val lowScorePercentage = String.format(scorePercentage, lowScoreRounded)

        with(binding) {
            resultImage.setImageURI(imageUriString)
            resultText.text = labelList?.get(0)
            tvHighScoreLabel.text = labelList?.get(0)
            tvLowScoreLabel.text = labelList?.get(1)
            tvHighScoreValue.text = highScorePercentage
            tvLowScoreValue.text = lowScorePercentage
            lpiHighScore.progress = highScoreRounded
            lpiLowScore.progress = lowScoreRounded
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}