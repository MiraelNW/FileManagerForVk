package com.miraeldev.filemanagerforvk.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

class FileOpener {
    companion object {
        fun openFile(context: Context, selectedFile: File) {
            val uri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                selectedFile
            )

            val intent = Intent(Intent.ACTION_VIEW)

            if (uri.toString().contains(".doc")) {
                intent.setDataAndType(uri, "application/msword")
            } else if (uri.toString().contains(".pdf")) {
                intent.setDataAndType(uri, "application/pdf")
            } else if (uri.toString().contains(".mp3") || uri.toString().contains(".wav")) {
                intent.setDataAndType(uri, "audio/x-wav")
            } else if (uri.toString().lowercase().contains(".jpeg")
                || uri.toString().lowercase().contains(".jpg")
                || uri.toString().lowercase().contains(".png")
            ) {
                intent.setDataAndType(uri, "image/jpeg")
            } else if (uri.toString().lowercase().contains(".mp4")) {
                intent.setDataAndType(uri, "video/*")
            } else {
                intent.setDataAndType(uri, "*/*")
            }

            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.startActivity(intent)
        }
    }
}