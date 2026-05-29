package com.bistpicker.mobile

import android.app.Application

class BistPickerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        val container = DefaultAppContainer(
            appContext = this,
            manifestUrl = BuildConfig.DEFAULT_MANIFEST_URL
        )
        container.bootstrap()
        AppContainerProvider.set(container)
    }
}
