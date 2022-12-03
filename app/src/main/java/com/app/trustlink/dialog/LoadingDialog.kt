package com.app.trustlink.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.app.trustlink.R

object LoadingDialog {
    private lateinit var alertDialog: AlertDialog

    fun startLoading(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.loading_dialog)
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun stopLoading () {
        alertDialog.dismiss()
    }
}