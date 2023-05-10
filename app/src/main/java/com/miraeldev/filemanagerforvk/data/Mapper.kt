package com.miraeldev.filemanagerforvk.data

import android.os.Build
import com.miraeldev.filemanagerforvk.data.database.FileDbModel
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import javax.inject.Inject

class Mapper @Inject constructor() {
    fun mapFileToFileModel(file: File): FileModel {
        return FileModel(
            absolutePath = file.absolutePath,
            name = file.name,
            size = file.length() / 1000,
            dateInMillis = file.getLastModifiedTimeInMillis(),
            isDirectory = file.isDirectory,
            type = if (!file.isDirectory) getType(file.absolutePath) else "directory",
            hashcode = file.hashCode()
        )
    }

    fun mapFileModelToFileDbModel(file: FileModel): FileDbModel {
        return FileDbModel(
            hashcode = file.hashcode,
            isDirectory = file.isDirectory,
            dateInMillis = file.dateInMillis,
            absolutePath = file.absolutePath,
            name = file.name,
            type = file.type,
            size = file.size
        )
    }

    fun mapFileDbModelToFileModel(file: FileDbModel): FileModel {
        return FileModel(
            hashcode = file.hashcode,
            isDirectory = file.isDirectory,
            dateInMillis = file.dateInMillis,
            name = file.name,
            type = file.type,
            size = file.size,
            absolutePath = file.absolutePath
        )
    }

    private fun getType(path: String): String {
        if (path.contains(".doc") || path.contains(".docx")) {
            return "doc"
        } else if (path.contains(".pdf")) {
            return "pdf"
        } else if (path.contains(".mp3")) {
            return "mp3"
        } else if (path.contains(".jpeg") || path.contains(".jpg") || path.contains(".png")) {
            return "jpeg"
        } else if (path.contains(".mp4")) {
            return "mp4"
        } else if (path.contains(".txt")) {
            return "txt"
        } else if (path.contains(".pptx")) {
            return "pptx"
        } else {
            return "*"
        }
    }

    private fun File.getLastModifiedTimeInMillis(): Long {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val basicFileAttributes = Files.readAttributes(
                this.toPath(),
                BasicFileAttributes::class.java
            )
            basicFileAttributes.creationTime().toMillis()
        } else {
            this.lastModified()
        }
    }

}