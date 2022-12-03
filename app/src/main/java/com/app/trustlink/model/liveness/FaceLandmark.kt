package com.app.trustlink.model.liveness

import com.google.gson.annotations.SerializedName

data class FaceLandmark(
    @SerializedName("LeftEyeX")
    var leftEyeX: String? = null,

    @SerializedName("LeftEyeY")
    var leftEyeY: String? = null,

    @SerializedName("MouthLeftX")
    var mouthLeftX: String? = null,

    @SerializedName("MouthLeftY")
    var mouthLeftY: String? = null,

    @SerializedName("MouthRightX")
    var mouthRightX: String? = null,

    @SerializedName("MouthRightY")
    var mouthRightY: String? = null,

    @SerializedName("NoseX")
    var noseX: String? = null,

    @SerializedName("NoseY")
    var noseY: String? = null,

    @SerializedName("RightEyeX")
    var rightEyeX: String? = null,

    @SerializedName("RightEyeY")
    var rightEyeY: String? = null
)