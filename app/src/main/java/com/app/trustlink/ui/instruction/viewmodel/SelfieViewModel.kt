package com.app.trustlink.ui.instruction.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.trustlink.api.ApiClient
import com.app.trustlink.extension.Constant
import com.app.trustlink.extension.PrefManager
import com.app.trustlink.extension.Utils
import com.app.trustlink.model.obtain.ResponseObtain
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelfieViewModel: ViewModel() {

    fun obtain(
        context: Context,
        body: HashMap<String, String>
    ): MutableLiveData<ResponseObtain> {
        val result = MutableLiveData<ResponseObtain>()
        ApiClient.getClient(PrefManager.getString(context, Constant.BASE_URL_OBTAIN, "") ?: "")
            .obtain(
                body["username"], body["password"]
            )
            .enqueue(object : Callback<ResponseObtain> {
                override fun onResponse(
                    call: Call<ResponseObtain>,
                    response: Response<ResponseObtain>
                ) {
                    when (response.code()) {
                        200 -> {
                            result.value = response.body()
                        }

                        else -> {
                            val gson = Gson()
                            val type = object : TypeToken<ResponseObtain>() {}.type
                            val errorResponse = gson.fromJson<ResponseObtain>(response.errorBody()?.charStream(), type)
                            result.value = errorResponse

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseObtain>, t: Throwable) {
                    val res = ResponseObtain(null)
                    res.message = Utils.failureMessage(context, t)
                    result.value = res
                }

            })

        return result
    }
}