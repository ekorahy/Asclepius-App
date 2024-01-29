package com.dicoding.asclepius.view.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.history.History
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.utils.DataFormatter
import com.dicoding.asclepius.view.ViewModelFactory
import com.dicoding.asclepius.view.history.HistoryActivity
import com.dicoding.asclepius.view.news.NewsActivity
import com.dicoding.asclepius.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = obtainViewModel(this@MainActivity)

        mainViewModel.currentImageUri.observe(this) { uri ->
            showResult(uri)
            enableButtonAnalysis(uri != null)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }

        binding.btnNews.setOnClickListener {
            val intent = Intent(this@MainActivity, NewsActivity::class.java)
            startActivity(intent)
        }

        binding.btnHistory.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val dateAndTime = LocalDateTime.now()
            val nameFileFormat = getString(R.string.name_file)
            val outputImage = File(
                filesDir,
                String.format(nameFileFormat, dateAndTime, Random.nextInt(100) + 1)
            ).toUri()
            UCrop.of(uri, outputImage)
                .withAspectRatio(5f, 5f)
                .withMaxResultSize(1000, 1000)
                .start(this)
        } else {
            showToast(getString(R.string.no_media))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            resultUri?.let { mainViewModel.updateCurrentImageUri(it) }
            showToast(getString(R.string.photo_added))
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = data?.let { UCrop.getError(it) }
            showToast(cropError.toString())
        }
    }

    private fun analyzeImage() {
        showLoading(true)
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showLoading(false)
                    runOnUiThread {
                        showToast(error)
                    }
                }

                override fun onResult(results: List<Classifications>?) {
                    runOnUiThread {
                        val labelList = ArrayList<String>()
                        val scoreList = ArrayList<String>()
                        results?.let { result ->
                            if (results.isNotEmpty() && result[0].categories.isNotEmpty()) {
                                val sortedCategories =
                                    result[0].categories.sortedByDescending { it?.score }
                                for (category in sortedCategories) {
                                    val label = category.label
                                    val score = category.score
                                    labelList.add(label)
                                    scoreList.add(score.toString())
                                }
                            }
                        }
                        moveToResult(labelList, scoreList)
                    }
                    showLoading(false)
                }
            }
        )
        mainViewModel.currentImageUri.value?.let {
            imageClassifierHelper.classifyStaticImage(
                context = this,
                it
            )
        }
    }

    private fun moveToResult(
        labelList: ArrayList<String>,
        scoreList: ArrayList<String>
    ) {
        val imageUriString = mainViewModel.currentImageUri.value.toString()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(IMAGE_URI, imageUriString)
        intent.putExtra(LABEL_LIST, labelList)
        intent.putExtra(SCORE_LIST, scoreList)
        startActivity(intent)
        val history = History(
            imgSrc = imageUriString,
            highScoreLabel = labelList[0],
            highScoreValue = DataFormatter().rounded(scoreList[0].toFloat()),
            lowScoreLabel = labelList[1],
            lowScoreValue = DataFormatter().rounded(scoreList[1].toFloat()),
            date = LocalDate.now().toString()
        )
        mainViewModel.insert(history)
    }

    private fun showResult(imageUri: Uri?) {
        binding.previewImageView.setImageURI(imageUri)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun enableButtonAnalysis(isEnable: Boolean) {
        binding.analyzeButton.isEnabled = isEnable
    }

    companion object {
        const val IMAGE_URI = "image_uri"
        const val LABEL_LIST = "label_list"
        const val SCORE_LIST = "score_list"
    }
}