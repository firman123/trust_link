package com.app.trustlink.model.liveness

import com.google.gson.annotations.SerializedName

data class BoundingBox(
    @SerializedName("BottomRightX")
    var bottomRightX: String? = null,

    @SerializedName("BottomRightY")
    var bottomRightY: String? = null,

    @SerializedName("Height")
    var height: String? = null,

    @SerializedName("TopLeftX")
    var topLeftX: String? = null,

    @SerializedName("TopLeftY")
    var topLeftY: String? = null,

    @SerializedName("Width")
    var width: String? = null
)