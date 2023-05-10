package com.miraeldev.filemanagerforvk.presentation.ui.filesList

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import com.miraeldev.filemanagerforvk.domain.usecases.GetFilesListUseCase
import com.miraeldev.filemanagerforvk.domain.usecases.SaveAllFilesInDbUseCase
import kotlinx.coroutines.*
import javax.inject.Inject

class FileListViewModel @Inject constructor(
    private val saveAllFilesInDb: SaveAllFilesInDbUseCase,
    private val getFilesListUseCase: GetFilesListUseCase,
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> get() = _screenState
    private var changedFilesList:List<FileModel>? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        viewModelScope.launch {
            saveAllFilesInDb(Environment.getExternalStorageDirectory().path)
        }
    }

    fun getList(path: String) {
        _screenState.value = Loading
        viewModelScope.launch {
            val deferredList = scope.async {
                getFilesListUseCase(path)
            }
            val list = deferredList.await()
            _screenState.value = FileList(list)
        }
    }

    fun sortList(sortBy: Int) {
        val list = _screenState.value as FileList
        when (sortBy) {
            1 -> _screenState.value = FileList(list.listOfFileModels.sortedBy { it.size })
            2 -> _screenState.value = FileList(list.listOfFileModels.sortedBy { it.dateInMillis })
            3 -> _screenState.value = FileList(list.listOfFileModels.sortedBy { it.type })
            4 -> _screenState.value = FileList(list.listOfFileModels.sortedBy { it.name })
            5 -> _screenState.value = FileList(list.listOfFileModels.sortedByDescending { it.size })
            6 -> _screenState.value =
                FileList(list.listOfFileModels.sortedByDescending { it.dateInMillis })
            7 -> _screenState.value = FileList(list.listOfFileModels.sortedByDescending { it.type })
            8 -> _screenState.value = FileList(list.listOfFileModels.sortedByDescending { it.name })
            else -> _screenState.value = FileList(list.listOfFileModels.sortedBy { it.name })
        }
    }


}