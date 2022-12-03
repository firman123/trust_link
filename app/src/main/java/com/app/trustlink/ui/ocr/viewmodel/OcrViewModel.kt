package com.app.trustlink.ui.ocr.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.trustlink.api.ApiClient
import com.app.trustlink.extension.Constant
import com.app.trustlink.extension.PrefManager
import com.app.trustlink.extension.Utils
import com.app.trustlink.model.ocr.ResponseOcr
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OcrViewModel: ViewModel() {

    fun sendOcr(
        context: Context,
        image: MultipartBody.Part
    ): MutableLiveData<ResponseOcr> {
        val result = MutableLiveData<ResponseOcr>()
        ApiClient.getClient(PrefManager.getString(context, Constant.BASE_URL_OCR, "") ?:"")
            .sendOcr(PrefManager.getString(context, Constant.END_POINT_OCR, "")?:"",PrefManager.getString(context, Constant.API_KEY_OCR, ""),
                image
            ).enqueue(object : Callback<ResponseOcr> {
                override fun onResponse(
                    call: Call<ResponseOcr>,
                    response: Response<ResponseOcr>
                ) {
                    when (response.code()) {
                        200 -> {
                            result.value = response.body()
                        }

                        else -> {
                            val gson = Gson()
                            val type = object : TypeToken<ResponseOcr>() {}.type
                            val errorResponse: ResponseOcr =
                                gson.fromJson(response.errorBody()!!.charStream(), type)
                            result.value = errorResponse

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseOcr>, t: Throwable) {
                    val res = ResponseOcr(null)
                    res.message = Utils.failureMessage(context, t)
                    result.value = res
                }

            })
        return result
    }
}