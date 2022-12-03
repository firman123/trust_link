package com.app.trustlink.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.trustlink.databinding.ActivityMainBinding
import com.app.trustlink.dialog.LoadingDialog
import com.app.trustlink.extension.Constant
import com.app.trustlink.extension.Constant.API_KEY_APIGEE
import com.app.trustlink.extension.Constant.API_KEY_OCR
import com.app.trustlink.extension.Constant.API_SECREET_APIGEE
import com.app.trustlink.extension.Constant.BASE_URL_OCR
import com.app.trustlink.extension.Constant.END_POINT_OCR
import com.app.trustlink.extension.Constant.TITLE_DUKCAPIL_TOKEN
import com.app.trustlink.extension.Constant.TITLE_ENCRYPTION
import com.app.trustlink.extension.Constant.TITLE_OBTAIN
import com.app.trustlink.extension.Constant.TITLE_OCR
import com.app.trustlink.extension.Constant.TITLE_PASSIVE_LIVENESS
import com.app.trustlink.extension.Constant.TITLE_SERVICES_TASK
import com.app.trustlink.extension.PrefManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readData()
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, InstructionActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }


    private fun readData() {
        runBlocking {
            LoadingDialog.startLoading(this@MainActivity)
            launch {
                getKeyData()
            }

            launch {
                getUrlData()
            }
        }
    }

    private fun getKeyData() {
        db.collection("api_key")
            .get()
            .addOnSuccessListener {
                Log.d("", "getKeyData: apikey")
                for (document in it) {
                    val apigee = document.data["apigee"].toString()
                    val ocr = document.data["ocr"].toString()
                    val secreetApigee = document.data["secreet_apigee"].toString()

                    PrefManager.putString(this, API_KEY_APIGEE, apigee)
                    PrefManager.putString(this, API_KEY_OCR, ocr)
                    PrefManager.putString(this, API_SECREET_APIGEE, secreetApigee)
                }

            }
            .addOnFailureListener {
                LoadingDialog.stopLoading()
                Toast.makeText(this, "Failed downdload data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUrlData() {
        db.collection("endpoint_trustlink")
            .get()
            .addOnSuccessListener {
                Log.d("TAG", "getUrlData: url")

                for (document in it) {
                    val title = document.data["title"].toString()
                    val baseUrl = document.data["base_url"].toString()
                    val endPoint = document.data["endpoint"].toString()

                    if (title.equals(TITLE_OCR, true)) {
                        PrefManager.putString(this, BASE_URL_OCR, baseUrl)
                        PrefManager.putString(this, END_POINT_OCR, endPoint)
                    } else if (title.equals(TITLE_PASSIVE_LIVENESS, true)) {
                        PrefManager.putString(this, Constant.BASE_URL_PASSIVE, baseUrl)
                    } else if (title.equals(TITLE_OBTAIN, true)) {
                        PrefManager.putString(this, Constant.BASE_URL_OBTAIN, baseUrl)
                    } else if (title.equals(TITLE_SERVICES_TASK, false)) {
                        PrefManager.putString(this, Constant.BASE_URL_SERVICES_TASK, baseUrl)
                    } else if (title.equals(TITLE_ENCRYPTION, true)) {
                        PrefManager.putString(this, Constant.BASE_URL_ENCRYPTION, baseUrl)
                    } else if (title.equals(TITLE_DUKCAPIL_TOKEN, true)) {
                        PrefManager.putString(this, Constant.BASE_URL_DUKCAPIL_TOKEN, baseUrl)
                        PrefManager.putString(this, Constant.END_POINT_DUKCAPIL_TOKEN, endPoint)
                    } else {
                        PrefManager.putString(this, Constant.BASE_URL_DUKCAPIL, baseUrl)
                    }
                }

                LoadingDialog.stopLoading()
            }
            .addOnFailureListener {
                LoadingDialog.stopLoading()
                Toast.makeText(this, "Failed downdload data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 20
        val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}