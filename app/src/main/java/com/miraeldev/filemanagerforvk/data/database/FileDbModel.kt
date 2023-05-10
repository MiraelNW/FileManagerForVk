package com.miraeldev.filemanagerforvk.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FileList")
data class FileDbModel(
    @PrimaryKey
    val hashcode: Int,
    val absolutePath: String,
    val name: String,
    val size: Long,
    val dateInMillis: Long,
    val isDirectory: Boolean,
    val type: String,
)