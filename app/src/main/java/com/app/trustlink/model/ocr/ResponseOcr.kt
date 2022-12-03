
package com.app.trustlink.model.ocr
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseOcr(
    var condition: Condition? = null,
    var read: Read? = null,
    var reason: String? = null,
    var status: String? = null,
    var message: String? = null
): Parcelable

@Parcelize
data class Condition(
    @SerializedName("is_blurred")
    var isBlurred: Boolean?,
    @SerializedName("is_bright")
    var isBright: Boolean?,
    @SerializedName("is_copy")
    var isCopy: Boolean?,
    @SerializedName("is_cropped")
    var isCropped: Boolean?,
    @SerializedName("is_dark")
    var isDark: Boolean?,
    @SerializedName("is_flash")
    var isFlash: Boolean?,
    @SerializedName("is_ktp")
    var isKtp: Boolean?,
    @SerializedName("is_rotated")
    var isRotated: Boolean?
): Parcelable

@Parcelize
data class Read(
    var agama: Agama?,
    var alamat: Alamat?,
    var berlakuHingga: BerlakuHingga?,
    var golonganDarah: GolonganDarah?,
    var jenisKelamin: JenisKelamin?,
    var kecamatan: Kecamatan?,
    var kelurahanDesa: KelurahanDesa?,
    var kewarganegaraan: Kewarganegaraan?,
    var kotaKabupaten: KotaKabupaten?,
    var nama: Nama?,
    var nik: Nik?,
    var pekerjaan: Pekerjaan?,
    var provinsi: Provinsi?,
    var rtRw: RtRw?,
    var statusPerkawinan: StatusPerkawinan?,
    var tanggalLahir: TanggalLahir?,
    var tempatLahir: TempatLahir?
): Parcelable

@Parcelize
data class Agama(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class Alamat(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class BerlakuHingga(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class GolonganDarah(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class JenisKelamin(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class Kecamatan(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class KelurahanDesa(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class Kewarganegaraan(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class KotaKabupaten(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class Nama(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class Nik(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class Pekerjaan(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class Provinsi(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class RtRw(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class StatusPerkawinan(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class TanggalLahir(
    var confidence: Int?,
    var value: String?
): Parcelable

@Parcelize
data class TempatLahir(
    var confidence: Int?,
    var value: String?
): Parcelable