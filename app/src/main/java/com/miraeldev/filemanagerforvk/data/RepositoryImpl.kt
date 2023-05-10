package com.miraeldev.filemanagerforvk.data

import android.util.Log
import com.miraeldev.filemanagerforvk.data.database.FileListDao
import com.miraeldev.filemanagerforvk.domain.Repository
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val fileListDao: FileListDao,
    private val mapper: Mapper
) : Repository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private var currPath : String? = null

    override fun getFilesList(path: String): List<FileModel> {
        if (currPath == null) emptyList<FileModel>()
        currPath = path
        val file = File(path)
        listFilesWithSubFolders(file)
        val fileModelsList = mutableListOf<FileModel>()
        file.listFiles()?.let { list ->
            list.forEach { file ->
                fileModelsList.add(mapper.mapFileToFileModel(file))
            }
        }
        scope.launch {
            fileModelsList.forEach { file ->
                if (!file.isDirectory) {
                    fileListDao.insertFile(mapper.mapFileModelToFileDbModel(file))
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
            fileListDao.insertFile(mapper.mapFileModelToFileDbModel(fileModel))
        }
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
}