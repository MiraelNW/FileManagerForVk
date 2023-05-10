package com.miraeldev.filemanagerforvk.domain.usecases

import com.miraeldev.filemanagerforvk.domain.Repository
import javax.inject.Inject

class GetChangedFilesListUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.getChangedFileList()
}