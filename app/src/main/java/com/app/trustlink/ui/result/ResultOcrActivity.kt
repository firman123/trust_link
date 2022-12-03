package com.app.trustlink.ui.result

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.trustlink.databinding.ActivityResultOcrBinding
import com.app.trustlink.extension.Constant
import com.app.trustlink.extension.PrefManager
import com.app.trustlink.model.ocr.ResponseOcr
import com.app.trustlink.ui.dukcapil.IdentifyVerificationActivity
import com.app.trustlink.ui.instruction.SelfieInstructionActivity
import com.app.trustlink.ui.ocr.UploadActivity
import android.view.ScaleGestureDetector





class ResultOcrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultOcrBinding
    private var ocrResult: ResponseOcr? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultOcrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBundleData()
        actionListener()
    }

    private fun actionListener() {
        binding.tvCopy.setOnClickListener {
            copyText()
        }

        binding.btnRetake.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
            finish()
        }

        binding.btnContinue.setOnClickListener {
            startActivity(Intent(this, SelfieInstructionActivity::class.java).putExtra("nik", binding.etNik.text.toString()))
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun copyText() {
        val text = binding.etNik.text.toString()
        if (text.isNotEmpty()) {
            PrefManager.putString(this, Constant.ID_KTP, text)
            Toast.makeText(this, "Id number already copied", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No text to be copied", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initBundleData() {
        val bundle = intent.extras
        bundle?.let {
            bundle.apply {
                ocrResult = getParcelable("data")
                val ktp = ocrResult?.read
                if (ktp != null) {

                    binding.etNik.setText(ktp.nik?.value)
                    binding.etNama.setText(ktp.nama?.value)
                    binding.etTtl.setText(ktp.tempatLahir?.value + " / " + ktp.tanggalLahir?.value)
                    binding.etJk.setText(ktp.jenisKelamin?.value)
                    binding.etAlamat.setText(ktp.alamat?.value)
                    binding.etRtrw.setText(ktp.rtRw?.value)
                    binding.etDesa.setText(ktp.kelurahanDesa?.value)
                    binding.etKecamatan.setText(ktp.kecamatan?.value)
                    binding.etKabupaten.setText(ktp.kotaKabupaten?.value)
                    binding.etProvinsi.setText(ktp.provinsi?.value)
                    binding.etAgama.setText(ktp.agama?.value)
                    binding.etStatusPerkawinan.setText(ktp.statusPerkawinan?.value)
                    Log.d("", "initBundleData: ${ktp.pekerjaan?.value}")
                    binding.etPekerjaan.setText(ktp.pekerjaan?.value)
                    binding.etKewarganegaraan.setText(ktp.kewarganegaraan?.value)
                    binding.etBerlakuHingga.setText(ktp.berlakuHingga?.value)

//                    ktp.nik = binding.etNik.text.toString()
//                    ktp.nama = binding.etNama.text.toString()
//                    ktp.agama = binding.etAgama.text.toString()
//                    ktp.pekerjaan = binding.etPekerjaan.text.toString()
//
//                    val jsonKtp = Gson().toJson(ktp)
//                    PrefManager.putString(
//                        this@ResultActivity,
//                        Constant.PREFERENCE.DATA_KTP,
//                        jsonKtp
//                    )
//
//                    processDemography = true
//                    binding.btnContinue.text = "Process Demography"
                }
            }
        }
    }
}