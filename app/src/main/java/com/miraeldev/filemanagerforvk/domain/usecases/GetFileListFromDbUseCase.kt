package com.miraeldev.filemanagerforvk.domain.usecases

import com.miraeldev.filemanagerforvk.domain.Repository
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import javax.inject.Inject

class GetFileListFromDbUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getFileListFromDb()
}