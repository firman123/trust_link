package com.app.trustlink.ui.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.app.trustlink.R
import com.app.trustlink.databinding.ActivityRearCameraBinding
import com.app.trustlink.ui.ocr.PreviewActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class   RearCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRearCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    private lateinit var savedUri: Uri
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var photoFile: File
    private lateinit var cameraExecutor: ExecutorService

    private var isFrontCamera = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRearCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCamera()
        initOutputDirectory()
        actionListener()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun initOutputDirectory() {
        outputDirectory = getOutputDirectory()
    }


    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }


    private fun actionListener() {
        binding.btnCamera.setOnClickListener {
            takePhoto()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }


    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT,
                Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    cameraProvider.unbindAll()
                    binding.viewFinder.visibility = View.GONE

                    val intent = Intent(this@RearCameraActivity, PreviewActivity::class.java)
                    intent.putExtra("image_path", photoFile.path)
                   startActivity(intent)
                    finish()
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            //preview
            val preview = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display?.rotation?.let { it ->
                    Preview.Builder()
                        .setTargetRotation(it)
                        .build()
                        .also {
                            it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                        }
                }
            } else {
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.rotation.let {
                    Preview.Builder()
                        .setTargetRotation(it)
                        .build()
                        .also { data ->
                            data.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                        }
                }
            }


            imageCapture = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display?.rotation?.let {
                    ImageCapture.Builder()
                            .setTargetRotation(it)
                        .build()
                }
            } else {
                windowManager.defaultDisplay.rotation.let {
                    ImageCapture.Builder()
                        .setTargetRotation(it)
                        .build()
                }
            }

//            val orientationEventListener = object : OrientationEventListener(this) {
//                override fun onOrientationChanged(orientation : Int) {
//                    // Monitors orientation values to determine the target rotation value
//                    val rotation : Int = when (orientation) {
//                        in 45..134 -> Surface.ROTATION_270
//                        in 135..224 -> Surface.ROTATION_180
//                        in 225..314 -> Surface.ROTATION_90
//                        else -> Surface.ROTATION_0
//                    }
//
//                    imageCapture?.targetRotation = rotation
//                }
//            }
//            orientationEventListener.enable()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    companion object {
        private const val TAG = "FaceCamera"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}