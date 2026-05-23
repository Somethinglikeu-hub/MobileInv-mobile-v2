package com.bistpicker.mobile

import android.app.Application

class BistPickerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        val container = DefaultAppContainer(
            appContext = this,
            manifestUrl = if (BuildConfig.DEBUG) {
                "http://10.0.2.2:8000/manifest.json"
            } else {
                BuildConfig.DEFAULT_MANIFEST_URL
            }
        )
        container.bootstrap()
        AppContainerProvider.set(container)
    }
}
