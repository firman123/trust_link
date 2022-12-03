package com.app.trustlink.extension


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Base64.encodeToString
import java.io.*


class ConverterImage {
    fun convertToBase64(context: Context, imageUri: Uri): String {
        val imageStream = context.contentResolver.openInputStream(imageUri)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        return encodeImage(selectedImage)
    }

    fun encodeImage(bitmap: Bitmap?): String {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 20, stream)
        val bytes = stream.toByteArray()
        return encodeToString(bytes, Base64.DEFAULT)
    }

}