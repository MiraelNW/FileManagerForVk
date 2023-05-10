package com.miraeldev.filemanagerforvk.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import com.miraeldev.filemanagerforvk.domain.usecases.GetFileListFromDbUseCase
import com.miraeldev.filemanagerforvk.domain.usecases.GetFilesListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FileListViewModel @Inject constructor(
    private val getFilesListUseCase: GetFilesListUseCase,
    private val getFileListFromDbUseCase: GetFileListFromDbUseCase,
) : ViewModel() {

    private val fileListMLD = MutableLiveData<List<FileModel>>()
    val fileListLD: LiveData<List<FileModel>> get() = fileListMLD

    fun getList(path: String) {
        fileListMLD.value = getFilesListUseCase(path)
        viewModelScope.launch {
            getFileListFromDbUseCase()
        }
    }

    fun sortList(sortBy: Int) {
        when (sortBy) {
            1 -> fileListMLD.value = fileListMLD.value?.sortedBy { it.size }
            2 -> fileListMLD.value = fileListMLD.value?.sortedBy { it.dateInMillis }
            3 -> fileListMLD.value = fileListMLD.value?.sortedBy { it.type }
            4 -> fileListMLD.value = fileListMLD.value?.sortedBy { it.name }
            5 -> fileListMLD.value = fileListMLD.value?.sortedByDescending { it.size }
            6 -> fileListMLD.value = fileListMLD.value?.sortedByDescending { it.dateInMillis }
            7 -> fileListMLD.value = fileListMLD.value?.sortedByDescending { it.type }
            8 -> fileListMLD.value = fileListMLD.value?.sortedByDescending { it.name }
            else -> fileListMLD.value = fileListMLD.value?.sortedBy { it.name }
        }
    }
}