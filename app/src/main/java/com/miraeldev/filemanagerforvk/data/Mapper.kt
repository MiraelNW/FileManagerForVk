package com.miraeldev.filemanagerforvk.data

import android.os.Build
import com.miraeldev.filemanagerforvk.data.database.FileDbModel
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import javax.inject.Inject

class Mapper @Inject constructor() {
    fun mapFileToFileModel(file: File, fileSize: Long = file.length()): FileModel {
        return FileModel(
            absolutePath = file.absolutePath,
            name = file.name,
            size = fileSize,
            formatSize = formatSize(fileSize),
            dateInMillis = file.getLastModifiedTimeInMillis(),
            isDirectory = file.isDirectory,
            type = if (!file.isDirectory) getType(file.absolutePath) else "directory",
            hashcode = file.hashCode().toString()
        )
    }

    fun mapFileToFileDbModel(file: File, fileSize: Long = file.length()): FileDbModel {
        return FileDbModel(
            absolutePath = file.absolutePath,
            name = file.name,
            size = fileSize,
            dateInMillis = file.getLastModifiedTimeInMillis(),
            isDirectory = file.isDirectory,
            type = if (!file.isDirectory) getType(file.absolutePath) else "directory",
            hashcode = file.hashCode().toString()
        )
    }

    fun mapFileModelToFileDbModel(file: FileModel, fileHashCode: String): FileDbModel {
        return FileDbModel(
            hashcode = fileHashCode,
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
            formatSize = formatSize(file.size),
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

    private fun formatSize(size: Long): String {
        return buildString {
            append("File size: ")
            if (size < 999) {
                append(size)
                append(" Bytes")
            } else if (size < 999_999) {
                append(size / 1_000)
                append(" Kb")
            } else if (size < 999_999_999) {
                append(size / 1_000_000)
                append(" Mb")
            } else {
                append(size / 1_000_000_000)
                append(" Gb")
            }
        }
    }


}