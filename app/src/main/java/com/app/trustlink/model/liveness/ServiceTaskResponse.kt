package com.app.trustlink.model.liveness

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ServiceTaskResponse(
    var id: Int ? = null,
    var result: Result? = null,
    var status: String? = null,
    var message: String? = null
): Parcelable

@Parcelize
data class Result(
    val data: Data,
    val status: Int
): Parcelable

@Parcelize
data class Data(
    val liveness: DataLiveNess?= null,
    val issues: Issues?=null
): Parcelable

@Parcelize
data class DataLiveNess(
    val data: DataX ? = null,
    val result: Boolean?= null
): Parcelable

@Parcelize
data class Issues(
    val liveness: LivenessIssues?=null
): Parcelable

@Parcelize
data class  LivenessIssues(
    @SerializedName("FACE_NOT_FOUND")
    val faceNotFound: String?=null,
    @SerializedName("FACE_TOO_CLOSE")
    val faceTooClose: String?=null,
    @SerializedName("EYES_CLOSED")
    val eyesClosed: String?=null,
    @SerializedName("FACE_CLOSE_TO_BORDER")
    val faceCloseToBorder: String?=null,
    @SerializedName("FACE_CROPPED")
    val faceCropped: String?=null,
    @SerializedName("FACE_IS_OCCLUDED")
    val faceIsOccluded: String?=null,
    @SerializedName("TOO_MANY_FACES")
    val toManyFace: String? = null,
    @SerializedName("FACE_TOO_SMALL")
    val faceTooSmall: String? = null
): Parcelable

@Parcelize
data class DataX(
    val probability: Double? = null,
    val quality: Double? = null,
    val score: Double? = null,
    val error: String? = null,
    @SerializedName("error_code")
    val errorCode: String?=null
): Parcelable