package com.app.trustlink.ui.dukcapil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.trustlink.databinding.ActivityIdentifyVerificationBinding
import com.app.trustlink.dialog.LoadingDialog
import com.app.trustlink.extension.*
import com.app.trustlink.model.dukcapil.Dukcapil
import com.app.trustlink.model.liveness.Result
import com.app.trustlink.ui.camera.CameraActivity
import com.app.trustlink.ui.dukcapil.viewmodel.IdentifyViewModel
import com.app.trustlink.ui.instruction.viewmodel.SelfieViewModel
import com.axiata.pocxl.dialog.ConfirmationDialog
import com.axiata.pocxl.dialog.SuccessDialog
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream
import java.util.*

class IdentifyVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdentifyVerificationBinding
    private lateinit var viewModel: IdentifyViewModel
    private lateinit var selfieViewModel: SelfieViewModel

    private var id = ""
    private var imagePath = ""
    private var nik: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifyVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBundleData()
        initViewModel()
        actionListener()
        obtainToken()
    }

    private fun initBundleData() {
        val extras = intent.extras
        if (extras != null && !extras.getString("nik").isNullOrEmpty()) {
            nik = extras.getString("nik")
            binding.etNik.setText(nik)
        }
    }

    private fun actionListener() {
        binding.btnTakePhoto.setOnClickListener {
            resultLauncer.launch(Intent(this, CameraActivity::class.java))
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnVerifyLiveness.setOnClickListener {
            if(binding.etNik.text.toString().isEmpty()) {
                binding.etNik.error = "Nik tidak boleh kosong"
                return@setOnClickListener
            }

            ConfirmationDialog.showDialog(this, object: ConfirmationDialog.HandleDialogListener {
                override fun ok() {
                    sendEncryption(binding.etNik.text.toString())
                }

                override fun cancel() {

                }

            })
        }

        binding.btnRetake.setOnClickListener {
            resultLauncer.launch(Intent(this, CameraActivity::class.java))
        }
    }

    private fun getFileFromAssets(context: Context, fileName: String): File = File(context.cacheDir, fileName)
        .also {
            if (!it.exists()) {
                it.outputStream().use { cache ->
                    context.assets.open(fileName).use { inputStream ->
                        inputStream.copyTo(cache)
                    }
                }
            }
        }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[IdentifyViewModel::class.java]
        selfieViewModel = ViewModelProvider(this)[SelfieViewModel::class.java]
    }


    @OptIn(DelicateCoroutinesApi::class)
    private var resultLauncer =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.ivCameraIcon.visibility = View.GONE

                lifecycleScope.launch {
                    LoadingDialog.startLoading(this@IdentifyVerificationActivity)
                }
                val data: Intent? = it.data
                imagePath = data?.getStringExtra("image_path") ?: ""
                setImage(imagePath)

                GlobalScope.launch(Dispatchers.Main) {
                    sendPassiveLiveness(imagePath)
                }
                binding.btnTakePhoto.visibility = View.GONE
                binding.constraintLayout2.visibility = View.VISIBLE

            }
        }

    private fun setImage(imagePath: String) {
        lifecycleScope.launch {
            binding.ivPhoto.setImageBitmap(RotateImageHelper.rotateImage(imagePath))
        }

    }

    private fun obtainToken() {
        LoadingDialog.startLoading(this)
        val body = HashMap<String, String>()
        body["username"] = "ext_dev"
        body["password"] = "EDev@12345"
        selfieViewModel.obtain(this, body).observe(this) {
            LoadingDialog.stopLoading()
            if (it.message.isNullOrEmpty()) {
                PrefManager.putString(this, Constant.ACCESS_TOKEN, it?.access?:"")
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendEncryption (nik: String) {
        LoadingDialog.startLoading(this)
        val hashMap = HashMap<String, String>()
        hashMap["User_id"] = "admin"
        hashMap["Password"] = "password"
        hashMap["NIK"] = nik
        val mapValue = Gson().toJson(hashMap)
        val data: RequestBody = mapValue.toRequestBody("text/plain".toMediaTypeOrNull())
        val file = getFileFromAssets(this, "public.pem")

        val requestBody =
            MultipartBody.Part.createFormData("filename", file.name, file.asRequestBody())
        viewModel.userEncrytion(this, requestBody, data)
            .observe(this) {
                if (it.message.isNullOrEmpty()) {
                    getTokenDukcapil(it.userId, it.password, it.nik)
                } else {
                    LoadingDialog.stopLoading()
                    Toast.makeText(
                        this,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun getTokenDukcapil(userId: String?, password: String?, nik: String?) {
        viewModel.dukcapilToken(this)
            .observe(this) {
                if(it.errorMessage.isNullOrEmpty()) {
                    sendDukcapil(it?.accessToken ?: "", userId, password, nik )
                } else {
                    LoadingDialog.stopLoading()
                    val intent = Intent(this, FailedActivity::class.java)
                    intent.putExtra("data", it)
                    intent.putExtra("type", "get_token")
                    startActivity(intent)
                }
            }
    }

    fun sendDukcapil(token: String, userId: String?, password: String?, nik: String?) {
        val imageString = ConverterImage().encodeImage(RotateImageHelper.rotateImage(imagePath)).replace("\n", "")
        val min = 100000000
        val max = 900000000
        val idTrans = Random().nextInt(max - min + 1) + min
        val dukcapil = Dukcapil(
            imageString,
            "95",
            "192.168.1.1",
            nik,
            password,
            true,
            idTrans.toString(),
            "MOBILE",
            userId)
        viewModel.dukcapil(this, token, dukcapil).observe(this) { it ->
            LoadingDialog.stopLoading()
            if(it?.errorMessage.isNullOrEmpty()) {
                val intent = Intent(this, FailedActivity::class.java)
                intent.putExtra("data", it)
                intent.putExtra("type", "dukcapil")

                if (it.error?.errorCode == 6018) {
                    startActivity(Intent(this, SuccessActivity::class.java))
                } else if(it.error?.errorCode!! in 501..6014) {
                    startActivity(intent)
                } else {
                    startActivity(intent)
                }

            } else {
                startActivity(Intent(this, FailedActivity::class.java).also {it
                    it.putExtra("data", it)
                    it.putExtra("type", "dukcapil")
                })
            }
        }
    }

    fun sendPassiveLiveness(image: String) {
        Log.d("send_data", "1")
        val extendValue = "true"
        val extend: RequestBody = extendValue.toRequestBody("text/plain".toMediaTypeOrNull())
        val file = File(image)
        file.copyInputStreamToFile(BitmapUtils.compressedImageFile(image))
        val requestBody =
            MultipartBody.Part.createFormData("image", file.name, file.asRequestBody())
        viewModel.liveness(this, requestBody, extend)
            .observe(this) {
                if (it.message.isNullOrEmpty()) {
                    id = it.id ?: ""
                    getSerivceTask(it.id ?: "")
                } else {
                    LoadingDialog.stopLoading()
                    Toast.makeText(
                        this,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun getSerivceTask(id: String) {
        viewModel.serviceTask(this, id).observe(this) {
            LoadingDialog.stopLoading()
            if (it.message.isNullOrEmpty()) {
                Log.d("message==2= ", it.message ?:"")

                binding.btnTakePhoto.visibility = View.GONE
                binding.constraintLayout2.visibility = View.VISIBLE
                if (it.status.equals(
                        "SUCCESS",
                        true
                    ) && it.result?.data?.liveness?.result == true
                ) {
                    SuccessDialog.showDialog(this)
                } else if (it.status.equals(
                        "SUCCESS",
                        true
                    ) && it.result?.data?.liveness?.result == false
                ) {
                    val intent = Intent(this, FailedDetectionActivity::class.java)
                    intent.putExtra("data", it)
                    intent.putExtra("type", "identity_verification")
                    startActivity(intent)
                } else {
                    startActivity(Intent(this, FailedDetectionActivity::class.java))
                }

            } else {
                startActivity(Intent(this, FailedDetectionActivity::class.java))
            }
        }
    }

    fun File.copyInputStreamToFile(inputStream: InputStream?) {
        this.outputStream().use { fileOut ->
            inputStream?.copyTo(fileOut)
        }
    }
}