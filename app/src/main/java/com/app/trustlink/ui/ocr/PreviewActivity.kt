package com.app.trustlink.ui.ocr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.trustlink.databinding.ActivityPreviewBinding
import com.app.trustlink.dialog.LoadingDialog
import com.app.trustlink.extension.BitmapUtils
import com.app.trustlink.extension.ConvertUriToFile
import com.app.trustlink.ui.instruction.SelfieInstructionActivity
import com.app.trustlink.ui.ocr.viewmodel.OcrViewModel
import com.app.trustlink.ui.result.ResultOcrActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min


class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private lateinit var viewModel: OcrViewModel
    private lateinit var uri: Uri
    private var imagePath: String? = null
    private var type = ""
    private var scaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initBundleData()
        initGesture()
        initActionListener()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[OcrViewModel::class.java]
    }

    private fun initBundleData() {
        val extras = intent.extras
        if (extras != null && !extras.getString("uri_image").isNullOrEmpty()) {
            val tempUri = extras.getString("uri_image")
            uri = Uri.parse(tempUri)
            setImageFromUri(uri)
            type = "gallery"
        } else if (extras != null && !extras.getString("image_path").isNullOrEmpty()) {
            imagePath = extras.getString("image_path")
            setImage(imagePath)
            type = "camera"
        }
    }

    private fun initGesture() {
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener(binding))
    }

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        scaleGestureDetector!!.onTouchEvent(motionEvent)
        return true
    }

    private class ScaleListener(var binding: ActivityPreviewBinding) : SimpleOnScaleGestureListener() {
        private var mScaleFactor = 1.0f

        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = max(0.1f, min(mScaleFactor, 10.0f))
            binding.ivResult.scaleX = mScaleFactor
            binding.ivResult.scaleY = mScaleFactor
            return true
        }
    }

    private fun initActionListener() {
        binding.btnUpload.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                LoadingDialog.startLoading(this@PreviewActivity)
                uploadFile()
            }
        }

        binding.btnRetake.setOnClickListener {
            finish()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvSkip.setOnClickListener {
            startActivity(Intent(this, SelfieInstructionActivity::class.java))
        }
    }

    private fun setImage(imagePath: String?) {
        lifecycleScope.launch {

//            binding.ivResult.scaleX = 1.5f
//            binding.ivResult.scaleY = 1.1f

            Glide.with(this@PreviewActivity)
                .load(imagePath)
                .centerCrop()
                .into(binding.ivResult)
        }
    }

    private fun setImageFromUri(selectedImageUri: Uri) {
        val selectedImageBitmap: Bitmap
        try {
            selectedImageBitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    selectedImageUri
                )
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, selectedImageUri)
                ImageDecoder.decodeBitmap(source)
            }

            binding.ivResult.setImageBitmap(selectedImageBitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun uploadFile() {
        val file: File?
        if (type.equals("gallery", true)) {
            file = ConvertUriToFile.getFile(this, uri)
            file.copyInputStreamToFile(BitmapUtils.compressedImageFile( file.path?: ""))

        } else {
            file = imagePath?.let { File(it) }
            file?.copyInputStreamToFile(BitmapUtils.compressedImageFile(imagePath ?: ""))
        }

        val filePart = MultipartBody.Part.createFormData(
            "image",
            file?.name,
            file!!.asRequestBody()
        )

        viewModel.sendOcr(this, filePart).observe(this) {
            LoadingDialog.stopLoading()
            if (it.message.isNullOrEmpty() && it.status.equals("SUCCESS", true)) {
                Intent(this, ResultOcrActivity::class.java).apply {
                    putExtra("data", it)
                    startActivity(this)
                }
            } else {
                val condition = it.condition
                startActivity(Intent(this, FailedOcrActivity::class.java).putExtra("data", condition))
            }
        }

    }

    private fun File.copyInputStreamToFile(inputStream: InputStream?) {
        this.outputStream().use { fileOut ->
            inputStream?.copyTo(fileOut)
        }
    }
}