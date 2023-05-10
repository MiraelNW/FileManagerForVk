package com.miraeldev.filemanagerforvk.domain

import androidx.lifecycle.LiveData
import com.miraeldev.filemanagerforvk.domain.model.FileModel

interface Repository {
    suspend fun getFilesList(path: String): List<FileModel>
    suspend fun saveAllFilesInDb(path: String)

    fun getChangedFileList(): LiveData<List<FileModel>>
}