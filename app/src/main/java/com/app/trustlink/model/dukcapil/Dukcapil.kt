package com.app.trustlink.model.dukcapil
import com.google.gson.annotations.SerializedName


data class Dukcapil(
    var faceImage: String?,
    var faceThreshold: String?,
    var ip: String?,
    @SerializedName("NIK")
    var nIK: String?,
    var password: String?,
    var thresholdInPercentage: Boolean?,
    var transactionId: String?,
    var transactionSource: String?,
    @SerializedName("user_id")
    var userId: String?
)