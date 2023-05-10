package com.miraeldev.filemanagerforvk.presentation.ui

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miraeldev.filemanagerforvk.domain.usecases.GetFilesListUseCase
import com.miraeldev.filemanagerforvk.domain.usecases.SaveAllFilesInDbUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val saveAllFilesInDb: SaveAllFilesInDbUseCase
) : ViewModel() {

    init {

//        viewModelScope.launch {
//            saveAllFilesInDb(Environment.getExternalStorageDirectory().path)
//        }
    }
}