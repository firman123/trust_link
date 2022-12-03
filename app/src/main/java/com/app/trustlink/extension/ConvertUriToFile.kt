package com.app.trustlink.extension

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ConvertUriToFile {
    fun getFile(context: Context, uri: Uri): File {
        val destinationFilename = File(
            context.filesDir.path + File.separatorChar.toString() + queryName(
                context,
                uri
            )
        )
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                if (ins != null) {
                    createFileFromStream(
                        ins,
                        destinationFilename
                    )
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return destinationFilename
    }

    private fun createFileFromStream(ins: InputStream, destination: File) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }
}