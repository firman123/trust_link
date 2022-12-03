package com.app.trustlink.ui.dukcapil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.trustlink.databinding.ActivityFailedBinding
import com.app.trustlink.model.dukcapil.ResponseDukcapil
import com.app.trustlink.ui.result.JsonResultActivity

class FailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFailedBinding
    private var failedDukcapil: ResponseDukcapil? = null
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFailedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBundleData()

        binding.btnSee.setOnClickListener {
            val intent = Intent(this, JsonResultActivity::class.java)
            intent.putExtra("data", failedDukcapil)
            intent.putExtra("type", type)
            startActivity(intent)
        }

        binding.btnRetry.setOnClickListener {
            val intent = Intent(this, IdentifyVerificationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initBundleData() {
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                failedDukcapil = getParcelable("data")
                type = getString("type")
            }
        }
    }
}