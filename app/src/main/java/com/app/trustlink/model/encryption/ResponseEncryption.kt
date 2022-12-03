package com.app.trustlink.model.encryption
import com.google.gson.annotations.SerializedName


data class ResponseEncryption(
    var nik: String? = null,
    var password: String? = null,
    @SerializedName("user_id")
    var userId: String? = null,
    var message: String? = null
)