package com.bistpicker.mobile

import android.app.Application

class BistPickerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        val container = DefaultAppContainer(
            appContext = this,
            manifestUrl = "https://raw.githubusercontent.com/Somethinglikeu-hub/MobileInv-feed/gh-pages/manifest.json"
        )
        container.bootstrap()
        AppContainerProvider.set(container)
    }
}
