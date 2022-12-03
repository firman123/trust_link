package com.app.trustlink.ui.dukcapil


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trustlink.R
import com.app.trustlink.databinding.ActivityFailedDetectionBinding
import com.app.trustlink.model.liveness.ServiceTaskResponse
import com.app.trustlink.ui.ocr.adapter.ItemAdapter
import com.app.trustlink.ui.result.JsonResultActivity

class FailedDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFailedDetectionBinding
    private lateinit var items: List<String>
    private var serviceTaskResponse: ServiceTaskResponse? = null
    private var type: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFailedDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        initAdapter()
        initBundleData()
        initActionListener()
    }

    private fun initActionListener() {
        binding.btnRetry.setOnClickListener {
            startActivity(Intent(this, IdentifyVerificationActivity::class.java))
            finish()
        }

        binding.btnSee.setOnClickListener {
            val intent = Intent(this, JsonResultActivity::class.java)
            intent.putExtra("data", serviceTaskResponse)
            intent.putExtra("type", type)
            startActivity(intent)
        }
    }

    private fun loadData() {
        val data = resources.getStringArray(R.array.failed_list)
        items = data.toList()
    }

    private fun initAdapter() {
        binding.rvPerhatian.layoutManager = LinearLayoutManager(this)
        val rvAdapter = ItemAdapter(items)
        binding.rvPerhatian.adapter = rvAdapter
    }

    private fun initBundleData() {
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                serviceTaskResponse = getParcelable("data")
                type = getString("type")
            }
        }
    }
}