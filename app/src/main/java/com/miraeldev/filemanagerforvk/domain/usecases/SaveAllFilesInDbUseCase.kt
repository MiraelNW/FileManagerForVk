package com.miraeldev.filemanagerforvk.domain.usecases

import com.miraeldev.filemanagerforvk.domain.Repository
import javax.inject.Inject

class SaveAllFilesInDbUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(path: String) = repository.saveAllFilesInDb(path)
}