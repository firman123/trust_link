package com.app.trustlink.ui.ocr

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.app.trustlink.databinding.ActivityUploadBinding
import com.app.trustlink.ui.camera.RearCameraActivity
import com.app.trustlink.ui.instruction.SelfieInstructionActivity


class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActionListener()
    }

    private fun initActionListener() {
        binding.btnUpload.setOnClickListener {
            imageChooser()
        }

        binding.btnPhoto.setOnClickListener {
            startActivity(Intent(this, RearCameraActivity::class.java))
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvSkip.setOnClickListener {
            startActivity(Intent(this, SelfieInstructionActivity::class.java))
        }
    }

    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(i)
    }

    private var launchSomeActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode
            == RESULT_OK
        ) {
            val data = result.data
            // do your operation from here....
            if (data != null
                && data.data != null
            ) {
                val uri = data.data.toString()
                val intent = Intent(this, PreviewActivity::class.java)
                intent.putExtra("uri_image", uri)
                startActivity(intent)
            }
        }
    }


}