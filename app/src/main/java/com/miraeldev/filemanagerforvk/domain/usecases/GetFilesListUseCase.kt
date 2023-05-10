package com.miraeldev.filemanagerforvk.domain.usecases

import com.miraeldev.filemanagerforvk.domain.Repository
import javax.inject.Inject

class GetFilesListUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(path: String) = repository.getFilesList(path)
}