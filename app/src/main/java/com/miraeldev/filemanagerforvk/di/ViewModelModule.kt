package com.miraeldev.filemanagerforvk.di

import androidx.lifecycle.ViewModel
import com.miraeldev.filemanagerforvk.presentation.ui.filesList.FileListViewModel
import com.miraeldev.filemanagerforvk.presentation.ui.changedFilesList.ChangedFilesListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(FileListViewModel::class)
    fun bindFileListViewModel(viewModel: FileListViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ChangedFilesListViewModel::class)
    fun bindMainViewModel(viewModel: ChangedFilesListViewModel): ViewModel
}