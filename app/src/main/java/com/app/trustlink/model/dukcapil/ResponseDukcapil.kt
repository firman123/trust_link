package com.app.trustlink.model.dukcapil

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ResponseDukcapil(
    var error: Error? = null,
    var httpResponseCode: String? = null,
    var matchScore: String? = null,
    var quotaLimiter: String? = null,
    var transactionId: String? = null,
    var uid: String? = null,
    var verificationResult: Boolean? = false,
    var errorMessage: String? = null
): Parcelable

@Parcelize
data class Error(
    var errorCode: Int? = null,
    var errorMessage: String? = null
): Parcelable