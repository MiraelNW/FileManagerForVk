package com.miraeldev.filemanagerforvk.di

import com.miraeldev.filemanagerforvk.data.RepositoryImpl
import com.miraeldev.filemanagerforvk.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DomainModule {

    @Binds
    fun bindRepository(impl: RepositoryImpl): Repository
}