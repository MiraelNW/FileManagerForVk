package com.miraeldev.filemanagerforvk.presentation.ui.filesList

import com.miraeldev.filemanagerforvk.domain.model.FileModel

sealed class ScreenState

object Loading : ScreenState()

class FileList(val listOfFileModels: List<FileModel>) : ScreenState()

