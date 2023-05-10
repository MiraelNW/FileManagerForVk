package com.miraeldev.filemanagerforvk.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FileList")
data class FileDbModel(
    @PrimaryKey
    val absolutePath: String,
    val hashcode: String,
    val name: String,
    val size: Long,
    val dateInMillis: Long,
    val isDirectory: Boolean,
    val type: String,
)