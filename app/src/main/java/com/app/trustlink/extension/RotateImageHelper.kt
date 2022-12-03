package com.app.trustlink.extension


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.DisplayMetrics
import java.io.IOException

object RotateImageHelper {
    @Throws(IOException::class)
    fun rotateImage(path: String?): Bitmap? {
        val bitmap = BitmapFactory.decodeFile(path)
        var rotate = 0
        val exif = ExifInterface(path!!)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
        }
        val matrix = Matrix()
        matrix.postRotate(rotate.toFloat())
        val bitmapCreate = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, true
        )
        bitmapCreate.density = DisplayMetrics.DENSITY_DEFAULT
        return bitmapCreate
    }
}