package com.app.trustlink.ui.ocr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trustlink.R
import com.app.trustlink.databinding.ActivityFailedOcrBinding
import com.app.trustlink.model.ocr.Condition
import com.app.trustlink.ui.instruction.SelfieInstructionActivity
import com.app.trustlink.ui.ocr.adapter.ItemAdapter
import com.app.trustlink.ui.result.JsonResultActivity

class FailedOcrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFailedOcrBinding
    private lateinit var items : List<String>
    private var ocrResult: Condition? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFailedOcrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        initAdapter()
        initActionListener()
        initBundleData()
    }

    private fun initBundleData() {
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                ocrResult = getParcelable("data")
            }
        }
    }

    private fun initActionListener() {
        binding.btnSee.setOnClickListener {
            val intent = Intent(this@FailedOcrActivity, JsonResultActivity::class.java)
            intent.putExtra("data", ocrResult)
            intent.putExtra("type", "ocr")
            startActivity(intent)
        }

        binding.btnRetry.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
            finish()
        }

        binding.tvSkipp.setOnClickListener {
            startActivity(Intent(this, SelfieInstructionActivity::class.java))
        }
    }


    private fun loadData() {
        val data = resources.getStringArray(R.array.attention_list)
        items = data.toList()
    }

    private fun initAdapter() {
        binding.rvPerhatian.layoutManager = LinearLayoutManager(this)
        val rvAdapter =  ItemAdapter(items)
        binding.rvPerhatian.adapter = rvAdapter
    }
}