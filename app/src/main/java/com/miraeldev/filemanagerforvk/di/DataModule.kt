package com.miraeldev.filemanagerforvk.di

import android.app.Application
import com.miraeldev.filemanagerforvk.data.database.AppDatabase
import com.miraeldev.filemanagerforvk.data.database.FileListDao
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {
        @Provides
        fun provideFileListDao(application: Application): FileListDao {
            return AppDatabase.getInstance(application).fileListDao()
        }
    }

}