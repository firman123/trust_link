package com.app.trustlink.ui.dukcapil.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.trustlink.api.ApiClient
import com.app.trustlink.extension.Constant
import com.app.trustlink.extension.PrefManager
import com.app.trustlink.extension.Utils
import com.app.trustlink.model.dukcapil.Dukcapil
import com.app.trustlink.model.dukcapil.ResponseDukcapil
import com.app.trustlink.model.dukcapil.ResponseTokenDukcapil
import com.app.trustlink.model.encryption.ResponseEncryption
import com.app.trustlink.model.liveness.ResponseLiveness
import com.app.trustlink.model.liveness.ServiceTaskResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IdentifyViewModel: ViewModel() {

    fun liveness(
        context: Context,
        image: MultipartBody.Part,
        extendData: RequestBody
    ): MutableLiveData<ResponseLiveness> {
        val result = MutableLiveData<ResponseLiveness>()

        ApiClient.getClient(PrefManager.getString(context, Constant.BASE_URL_PASSIVE, "") ?: "")
            .faceLiveness(
                "Bearer ${PrefManager.getString(context, Constant.ACCESS_TOKEN, "")}",
                image, extendData
            ).enqueue(object : Callback<ResponseLiveness> {
                override fun onResponse(
                    call: Call<ResponseLiveness>,
                    response: Response<ResponseLiveness>
                ) {
                    when (response.code()) {
                        200 -> {
                            result.value = response.body()

                            serviceTask(context, result.value?.id)
                        }

                        else -> {
                            val gson = Gson()
                            val type = object : TypeToken<ResponseLiveness>() {}.type
                            val errorResponse: ResponseLiveness =
                                gson.fromJson(response.errorBody()!!.charStream(), type)
                            result.value = errorResponse

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseLiveness>, t: Throwable) {
                    val res = ResponseLiveness(null)
                    res.message = Utils.failureMessage(context, t)
                    result.value = res
                }

            })
        return result
    }

    fun serviceTask(
        context: Context,
        id: String?
    ): MutableLiveData<ServiceTaskResponse> {
        val result = MutableLiveData<ServiceTaskResponse>()

        Handler(Looper.getMainLooper()).postDelayed({
            ApiClient.getClient(
                PrefManager.getString(context, Constant.BASE_URL_SERVICES_TASK, "") ?: ""
            ).serviveTask(
                id,
                "Bearer ${PrefManager.getString(context, Constant.ACCESS_TOKEN, "")}"
            )
                .enqueue(object : Callback<ServiceTaskResponse> {
                    override fun onResponse(
                        call: Call<ServiceTaskResponse>,
                        response: Response<ServiceTaskResponse>
                    ) {
                        when (response.code()) {
                            200 -> {
                                result.value = response.body()
                            }

                            else -> {
                                val gson = Gson()
                                val type = object : TypeToken<ServiceTaskResponse>() {}.type
                                val errorResponse: ServiceTaskResponse =
                                    gson.fromJson(response.errorBody()!!.charStream(), type)
                                result.value = errorResponse

                            }
                        }
                    }

                    override fun onFailure(call: Call<ServiceTaskResponse>, t: Throwable) {
                        val res = ServiceTaskResponse(null)
                        res.message = Utils.failureMessage(context, t)
                        result.value = res
                    }

                })
        }, 5000)

        return result
    }

    fun userEncrytion(
        context: Context,
        pemFile: MultipartBody.Part,
        extendData: RequestBody
    ): MutableLiveData<ResponseEncryption> {
        val result = MutableLiveData<ResponseEncryption>()

        ApiClient.getClient(PrefManager.getString(context, Constant.BASE_URL_ENCRYPTION, "") ?: "")
            .userEncryption(
                pemFile, extendData
            ).enqueue(object : Callback<ResponseEncryption> {
                override fun onResponse(
                    call: Call<ResponseEncryption>,
                    response: Response<ResponseEncryption>
                ) {
                    Log.d("", "onResponse_code: ${response.code()}")
                    when (response.code()) {
                        200 -> {
                            result.value = response.body()
                        }

                        else -> {
                            val gson = Gson()
                            val type = object : TypeToken<ResponseEncryption>() {}.type
                            val errorResponse: ResponseEncryption =
                                gson.fromJson(response.errorBody()!!.charStream(), type)
                            result.value = errorResponse

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseEncryption>, t: Throwable) {
                    val res = ResponseEncryption(null)
                    res.message = Utils.failureMessage(context, t)
                    result.value = res
                }

            })
        return result
    }

    fun dukcapilToken(context: Context): MutableLiveData<ResponseTokenDukcapil> {
        val mergeApiKey = "${PrefManager.getString(context, Constant.API_KEY_APIGEE,"")}:${PrefManager.getString(context, Constant.API_SECREET_APIGEE, "")}".toByteArray()
        val keybase64 = Base64.encodeToString(mergeApiKey, Base64.DEFAULT ).filter { !it.isWhitespace() }
        val result = MutableLiveData<ResponseTokenDukcapil>()

        ApiClient.getClient(PrefManager.getString(context, Constant.BASE_URL_DUKCAPIL_TOKEN, "") ?: "")
            .dukcapilToken("accesstoken","client_credentials", "Basic $keybase64")
            .enqueue(object : Callback<ResponseTokenDukcapil> {
                override fun onResponse(call: Call<ResponseTokenDukcapil>, response: Response<ResponseTokenDukcapil>) {
                    Log.d("", "onResponse: ${response.code()}")
                    when (response.code()) {
                        200 -> {
                            result.postValue(response.body())
                        }
                        else -> {
                            val gson = Gson()
                            val type = object : TypeToken<ResponseTokenDukcapil>() {}.type
                            val errorResponse: ResponseTokenDukcapil =
                                gson.fromJson(response.errorBody()!!.charStream(), type)
                            result.value = errorResponse
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseTokenDukcapil>, t: Throwable) {
                    val res = ResponseTokenDukcapil(null)
                    res.errorMessage = Utils.failureMessage(context, t)
                    result.value = res
                }
            })
        return result
    }

    fun dukcapil(context: Context, token: String, body: Dukcapil): MutableLiveData<ResponseDukcapil> {
        val result = MutableLiveData<ResponseDukcapil>()
        Log.d("", "dukcapil: " + PrefManager.getString(context, Constant.BASE_URL_DUKCAPIL, ""))
        ApiClient.getClient(PrefManager.getString(context, Constant.BASE_URL_DUKCAPIL, "") ?: "")
            .dukcapil("Bearer $token", body)
            .enqueue(object : Callback<ResponseDukcapil> {
                override fun onResponse(call: Call<ResponseDukcapil>, response: Response<ResponseDukcapil>) {
                    when (response.code()) {
                        200 -> {
                            Log.d("CAMERA ", "onResponse_DATA : 200 ")
                            result.postValue(response.body())
                        }
                        else -> {
                            val gson = Gson()
                            val type = object : TypeToken<ResponseDukcapil>() {}.type
                            val errorResponse: ResponseDukcapil =
                                gson.fromJson(response.errorBody()!!.charStream(), type)
                            result.value = errorResponse
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseDukcapil>, t: Throwable) {
                    val res = ResponseDukcapil(null)
                    res.errorMessage = Utils.failureMessage(context, t)
                    result.value = res
                }
            })
        return result
    }
}