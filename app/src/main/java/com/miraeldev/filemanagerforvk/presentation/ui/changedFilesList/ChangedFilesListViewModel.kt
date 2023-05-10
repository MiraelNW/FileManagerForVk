package com.miraeldev.filemanagerforvk.presentation.ui.changedFilesList

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miraeldev.filemanagerforvk.domain.model.FileModel
import com.miraeldev.filemanagerforvk.domain.usecases.GetChangedFilesListUseCase
import com.miraeldev.filemanagerforvk.domain.usecases.GetFilesListUseCase
import com.miraeldev.filemanagerforvk.domain.usecases.SaveAllFilesInDbUseCase
import com.miraeldev.filemanagerforvk.presentation.ui.filesList.Loading
import com.miraeldev.filemanagerforvk.presentation.ui.filesList.ScreenState
import kotlinx.coroutines.*
import javax.inject.Inject

class ChangedFilesListViewModel @Inject constructor(
    val changedFilesListUseCase: GetChangedFilesListUseCase,
) : ViewModel() {

}