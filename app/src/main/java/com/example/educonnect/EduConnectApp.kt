package com.example.educonnect

import android.app.Application
import com.example.educonnect.utils.ThemeManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class EduConnectApp : Application() {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate() {
        super.onCreate()
        themeManager.applyPersistedTheme()
    }
}
