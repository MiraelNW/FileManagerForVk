package com.miraeldev.filemanagerforvk.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.miraeldev.filemanagerforvk.data.database.FileListDao
import com.miraeldev.filemanagerforvk.domain.Repository
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import com.miraeldev.filemanagerforvk.utils.HashUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.security.MessageDigest
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val fileListDao: FileListDao,
    private val mapper: Mapper
) : Repository {

    private val scopeForCalculating = CoroutineScope(Dispatchers.Default)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _changedFiles = MutableLiveData<List<FileModel>>()
    private val changedFiles: LiveData<List<FileModel>> get() = _changedFiles

    override suspend fun getFilesList(path: String): List<FileModel> {
        val file = File(path)
        scopeForCalculating.launch {
            listFilesWithSubFolders(file)
        }
        val fileModelsList = mutableListOf<FileModel>()
        file.listFiles()?.let { list ->
            list.forEach { file ->
                if (file.isDirectory) {
                    fileModelsList.add(
                        mapper.mapFileToFileModel(
                            file,
                            calculateDirectorySize(file)
                        )
                    )
                } else {
                    fileModelsList.add(mapper.mapFileToFileModel(file))
                }
            }
        }
        return fileModelsList.sortedBy { it.name }
    }

    override suspend fun saveAllFilesInDb(path: String) {
        val file = File(path)
        val fullFileList = listFilesWithSubFolders(file)
        val result = mutableListOf<FileModel>()
        fullFileList.forEach { file ->
            val fileModel = mapper.mapFileModelToFileDbModel(
                mapper.mapFileToFileModel(file),
                calculateHashCode(file)
            )
            val fileFromDb = fileListDao.getFileFromDb(fileModel.absolutePath)

            if (fileFromDb != null && fileFromDb.hashcode != fileModel.hashcode) {
                result.add(mapper.mapFileDbModelToFileModel(fileModel))
            }
            scope.launch {
                fileListDao.insertFile(fileModel)
            }
        }
        _changedFiles.value = result

    }

    override fun getChangedFileList():LiveData<List<FileModel>> {
        return changedFiles
    }

    private fun calculateHashCode(file: File): String {
        return HashUtils.getCheckSumFromFile(
            MessageDigest.getInstance(SHA_256),
            file
        )
    }

    private fun listFilesWithSubFolders(file: File): List<File> {
        val files = mutableListOf<File>()
        file.listFiles()?.let {
            for (file in file.listFiles()) {
                if (file.isDirectory) {
                    files.addAll(listFilesWithSubFolders(file))
                } else {
                    files.add(file)
                }
            }
        }
        return files
    }

    private fun calculateDirectorySize(file: File): Long {
        var size = 0L
        file.listFiles()?.let {
            for (file in file.listFiles()) {
                if (file.isDirectory) {
                    size += calculateDirectorySize(file)
                } else {
                    size += file.length()
                }
            }
        }
        return size
    }

    private companion object {
        private const val SHA_256 = "SHA-256"
    }
}