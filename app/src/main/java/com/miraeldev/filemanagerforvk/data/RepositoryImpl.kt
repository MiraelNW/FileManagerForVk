package com.miraeldev.filemanagerforvk.data

import android.provider.MediaStore
import com.miraeldev.filemanagerforvk.data.database.FileListDao
import com.miraeldev.filemanagerforvk.domain.Repository
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import com.miraeldev.filemanagerforvk.utils.HashUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.security.MessageDigest
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val fileListDao: FileListDao,
    private val mapper: Mapper
) : Repository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val scopeForCalculating = CoroutineScope(Dispatchers.Default)

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

    override suspend fun getFileListFromDb(): List<FileModel> {
        val listOfFiles = mutableListOf<FileModel>()
        fileListDao.getFileListFromDb().forEach {
            listOfFiles.add(mapper.mapFileDbModelToFileModel(it))
        }
        return listOfFiles
    }

    override suspend fun saveAllFilesInDb(path: String) {
        val file = File(path)
        val fullFileList = listFilesWithSubFolders(file)
        fullFileList.forEach { file ->
            val fileModel = mapper.mapFileToFileModel(file)
            fileListDao.insertFile(
                mapper.mapFileModelToFileDbModel(
                    fileModel,
                    calculateHashCode(file)
                )
            )
        }
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