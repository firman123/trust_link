package com.app.trustlink.ui.instruction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.trustlink.databinding.ActivitySelfieInstructionBinding
import com.app.trustlink.ui.dukcapil.IdentifyVerificationActivity
import com.app.trustlink.ui.ocr.UploadActivity

class SelfieInstructionActivity : AppCompatActivity() {
    private var nik: String? = ""
    private lateinit var binding: ActivitySelfieInstructionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelfieInstructionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBundleData()

        binding.btnVerifikasi.setOnClickListener {
            startActivity(Intent(this, IdentifyVerificationActivity::class.java).putExtra("nik", nik))
        }

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
            finish()
        }
    }

    private fun initBundleData() {
        val extras = intent.extras
        if (extras != null && !extras.getString("nik").isNullOrEmpty()) {
            nik = extras.getString("nik")
        }
    }
}