package com.miraeldev.filemanagerforvk.domain.model

data class FileModel(
    val hashcode: Int,
    val absolutePath: String,
    val name: String,
    val size: Long,
    val dateInMillis: Long,
    val isDirectory: Boolean,
    val type: String,
)