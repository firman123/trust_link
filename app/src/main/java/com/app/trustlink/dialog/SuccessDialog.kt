package com.axiata.pocxl.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.app.trustlink.R

object SuccessDialog {
    private lateinit var dialog: AlertDialog

    fun showDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_verifikasi_sukses)
        builder.setCancelable(true)

        dialog = builder.create()
        dialog.show()
    }

    fun hideDialog() {
        dialog.dismiss()
    }
}