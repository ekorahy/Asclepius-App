package com.dicoding.asclepius.view.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.time.LocalDateTime
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage()
            } ?: run {
                showToast(getString(R.string.warning_no_photo_added))
            }
        }

    }

    // Gallery
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
            val listUri = listOf(uri, outputImage)
            cropImage.launch(listUri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    // Crop image
    private val uCropContract = object: ActivityResultContract<List<Uri>, Uri>() {
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f, 5f)
                .withMaxResultSize(1000, 1000)

            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }
    }

    private val cropImage = registerForActivityResult(uCropContract) { uri ->
        currentImageUri = uri
        binding.previewImageView.setImageURI(currentImageUri)
        showToast(getString(R.string.photo_added))
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object: ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
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
                                val sortedCategories = result[0].categories.sortedByDescending { it?.score }
                                for (category in sortedCategories) {
                                    val label = category.label
                                    val score = category.score
                                    labelList.add(label)
                                    scoreList.add(score.toString())
                                }
                            }
                        }
                        currentImageUri?.let { moveToResult(it, labelList, scoreList) }
                    }
                }
            }
        )
        currentImageUri?.let { imageClassifierHelper.classifyStaticImage(context = this, it) }
    }

    private fun moveToResult(
        imageUri: Uri,
        labelList: ArrayList<String>,
        scoreList: ArrayList<String>
    ) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(IMAGE_URI, imageUri.toString())
        intent.putExtra(LABEL_LIST, labelList)
        intent.putExtra(SCORE_LIST, scoreList)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val IMAGE_URI = "image_uri"
        const val LABEL_LIST = "label_list"
        const val SCORE_LIST = "score_list"
    }
}