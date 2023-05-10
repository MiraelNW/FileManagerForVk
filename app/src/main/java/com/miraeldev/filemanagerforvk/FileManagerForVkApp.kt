package com.miraeldev.filemanagerforvk

import android.app.Application
import com.miraeldev.filemanagerforvk.di.DaggerApplicationComponent

class FileManagerForVkApp : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onTerminate() {
        super.onTerminate()

    }
}