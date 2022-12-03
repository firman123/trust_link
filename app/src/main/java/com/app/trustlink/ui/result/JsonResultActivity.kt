package com.app.trustlink.ui.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.trustlink.databinding.ActivityJsonResultBinding
import com.app.trustlink.model.dukcapil.ResponseDukcapil
import com.app.trustlink.model.dukcapil.ResponseTokenDukcapil
import com.app.trustlink.model.liveness.Result
import com.app.trustlink.model.liveness.ServiceTaskResponse
import com.app.trustlink.model.ocr.Condition
import com.app.trustlink.ui.dukcapil.IdentifyVerificationActivity
import com.app.trustlink.ui.instruction.SelfieInstructionActivity
import com.app.trustlink.ui.ocr.UploadActivity
import com.google.gson.GsonBuilder


class JsonResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJsonResultBinding
    private var ocrResult: Condition? = null
    private var tokenDukcapilResponse: ResponseTokenDukcapil? = null
    private var dukcapilResponse: ResponseDukcapil? = null
    private var serviceTaskResponse: ServiceTaskResponse? = null
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJsonResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBundleData()
        actionListener()
    }

    private fun initBundleData() {
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                val gson = GsonBuilder().setPrettyPrinting().create()
                type = getString("type").toString()
                when(type) {
                    "ocr" -> {
                        ocrResult = getParcelable("data")
                        val json = gson.toJson(ocrResult)
                        binding.etError.setText(json)
                    }

                    "get_token" -> {
                        tokenDukcapilResponse = getParcelable("data")
                        val json = gson.toJson(tokenDukcapilResponse)
                        binding.etError.setText(json)
                    }

                    "dukcapil" -> {
                        dukcapilResponse = getParcelable("data")
                        val json = gson.toJson(dukcapilResponse)
                        binding.etError.setText(json)
                    }

                    "identity_verification" -> {
                        serviceTaskResponse = getParcelable("data")
                        val json = gson.toJson(serviceTaskResponse)
                        binding.etError.setText(json)
                    }
                }
            }
        }
    }

    private fun actionListener() {
        binding.btnTryAgain.setOnClickListener {

            when (type) {
                "ocr" -> {
                    startActivity(Intent(this, UploadActivity::class.java))
                    finish()
                }

                "dukcapil" -> {
                    startActivity(Intent(this, IdentifyVerificationActivity::class.java))
                    finish()
                }

                "get_token" -> {
                    startActivity(Intent(this, IdentifyVerificationActivity::class.java))
                    finish()
                }

                "identity_verification" -> {
                    startActivity(Intent(this, IdentifyVerificationActivity::class.java))
                    finish()
                }
            }
        }

        binding.ivBack.setOnClickListener { finish() }

        binding.tvSkip.setOnClickListener {
            startActivity(Intent(this, SelfieInstructionActivity::class.java))
        }
    }
}