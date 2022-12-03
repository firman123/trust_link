package com.app.trustlink.api

import com.app.trustlink.extension.Constant
import com.app.trustlink.model.liveness.ResponseLiveness
import com.app.trustlink.model.dukcapil.Dukcapil
import com.app.trustlink.model.dukcapil.ResponseDukcapil
import com.app.trustlink.model.dukcapil.ResponseTokenDukcapil
import com.app.trustlink.model.encryption.ResponseEncryption
import com.app.trustlink.model.liveness.ServiceTaskResponse
import com.app.trustlink.model.obtain.ResponseObtain
import com.app.trustlink.model.ocr.ResponseOcr
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

//    @Multipart
//    @POST("{endpoint}")
//    fun sendOcr(@Path("endpoint") endPoint: String, @Header("x-api-key") apiKey: String?, @Part multipart: MultipartBody.Part): Call<ResponseOcr>

    @Multipart
    @POST
    fun sendOcr(@Url endPoint: String, @Header("x-api-key") apiKey: String?, @Part multipart: MultipartBody.Part): Call<ResponseOcr>


    @Multipart
    @POST(".")
    fun faceLiveness(@Header("Authorization") token: String?, @Part multipart: MultipartBody.Part, @Part("extend_data") extendData: RequestBody): Call<ResponseLiveness>

    @FormUrlEncoded
    @POST(".")
    fun obtain(@Field("username") username: String?, @Field("password") password: String?): Call<ResponseObtain>

    @Headers(Constant.HEADER.ACCEPT, Constant.HEADER.CONTENT_TYPE)
    @GET(".")
    fun serviveTask(@Query("id") id: String?, @Header("Authorization") token: String?): Call<ServiceTaskResponse>

    @Multipart
    @POST(".")
    fun userEncryption(@Part multipart: MultipartBody.Part, @Part("data") extendData: RequestBody): Call<ResponseEncryption>

    @Headers(Constant.HEADER.ACCEPT, Constant.HEADER.CONTENT_TYPE)
    @POST
    fun dukcapilToken(@Url endPoint: String, @Query("grant_type") granType: String?, @Header("Authorization") token: String?): Call<ResponseTokenDukcapil>

    @Headers(Constant.HEADER.ACCEPT, Constant.HEADER.CONTENT_TYPE)
    @POST(".")
    fun dukcapil(@Header("Authorization") token: String?, @Body body: Dukcapil): Call<ResponseDukcapil>

}