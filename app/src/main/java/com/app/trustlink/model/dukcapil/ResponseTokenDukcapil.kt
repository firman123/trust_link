package com.app.trustlink.model.dukcapil

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseTokenDukcapil(
    @SerializedName("access_token")
    var accessToken: String? = null,

    @SerializedName("expires_in")
    var expiresIn: String? = null,

    var errorCode: String? = null,

    var errorMessage: String? = null
): Parcelable