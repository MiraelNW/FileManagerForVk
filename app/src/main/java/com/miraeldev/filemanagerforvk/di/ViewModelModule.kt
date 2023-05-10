package com.miraeldev.filemanagerforvk.di

import androidx.lifecycle.ViewModel
import com.miraeldev.filemanagerforvk.presentation.ui.FileListViewModel
import com.miraeldev.filemanagerforvk.presentation.ui.MainViewModel
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
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}