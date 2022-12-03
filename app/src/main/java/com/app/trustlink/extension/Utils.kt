package com.app.trustlink.extension


import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.app.trustlink.BuildConfig
import com.app.trustlink.R
import com.google.gson.JsonSyntaxException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


object Utils {

    fun failureMessage(context: Context, t: Throwable?): String {
        if (BuildConfig.DEBUG) {
            return t?.localizedMessage ?: checkResponseErrorCode(context, 0)
        } else {
            return checkException(context, t?.javaClass?.name)
        }

    }

    private fun checkException(context: Context, exceptionName: String?): String {
        Log.d("This is base checkException => %s", exceptionName!!)
        return if (exceptionName == UnknownHostException::class.java.name || exceptionName == TimeoutException::class.java.name) {
            context.getString(R.string.error_connection)
        } else if (exceptionName == JsonSyntaxException::class.java.name) {
            context.getString(R.string.error_json_syntax)
        } else {
            context.getString(R.string.error_something)
        }
    }

    private fun checkResponseErrorCode(context: Context, code: Int?): String {
        return when (code) {
            401 -> context.getString(R.string.error_code_401)
            404 -> context.getString(R.string.error_code_404)
            405 -> context.getString(R.string.error_code_405)
            422 -> context.getString(R.string.error_code_422)
            500 -> context.getString(R.string.error_code_500)
            else -> context.getString(R.string.error_connection)
        }
    }

}