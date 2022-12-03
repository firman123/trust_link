package com.app.trustlink.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.trustlink.databinding.ActivityInstructionBinding
import com.app.trustlink.ui.ocr.UploadActivity

class InstructionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInstructionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenAccount.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}