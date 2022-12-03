package com.axiata.pocxl.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.app.trustlink.R
import com.app.trustlink.databinding.DialogKonfirmasiBinding

object ConfirmationDialog {
    private lateinit var alertDialog: AlertDialog
    private lateinit var binding: DialogKonfirmasiBinding

    fun showDialog(context: Context, listener: HandleDialogListener) {
        val builder = AlertDialog.Builder(context)
        binding = DialogKonfirmasiBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()

        binding.btnYakin.setOnClickListener {
            alertDialog.dismiss()
            listener.ok()
        }

        binding.btnKembali.setOnClickListener {
            alertDialog.dismiss()
            listener.cancel()
        }
    }

     interface HandleDialogListener {
         fun ok()
         fun cancel()
     }
}