package com.miraeldev.filemanagerforvk.di

import android.app.Application
import com.miraeldev.filemanagerforvk.presentation.ui.FilesListFragment
import com.miraeldev.filemanagerforvk.presentation.ui.MainActivity
import dagger.Binds
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DomainModule::class, DataModule::class,ViewModelModule::class])
interface ApplicationComponent {

    fun inject(fragment:FilesListFragment)

    fun inject(activity:MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}