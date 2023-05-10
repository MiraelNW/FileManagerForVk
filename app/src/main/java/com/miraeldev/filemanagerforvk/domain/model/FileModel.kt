package com.miraeldev.filemanagerforvk.domain.model

data class FileModel(
    val hashcode: String,
    val absolutePath: String,
    val name: String,
    val size: Long,
    val formatSize: String,
    val dateInMillis: Long,
    val isDirectory: Boolean,
    val type: String,
)