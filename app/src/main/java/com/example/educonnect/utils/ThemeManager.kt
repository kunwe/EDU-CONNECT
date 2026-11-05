package com.example.educonnect.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages in-app theme mode (Light, Dark, System default) and persists the preference.
 */
@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "ThemeManager"
        private const val PREF_NAME = "educonnect_theme_prefs"
        private const val KEY_THEME_MODE = "theme_mode"

        const val THEME_LIGHT = "Light"
        const val THEME_DARK = "Dark"
        const val THEME_SYSTEM = "System default"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Maps theme option name to AppCompatDelegate night mode integer.
     */
    fun getNightMode(themeName: String): Int {
        return when (themeName) {
            THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

    /**
     * Maps night mode integer to theme option name.
     */
    fun getThemeName(nightMode: Int): String {
        return when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> THEME_LIGHT
            AppCompatDelegate.MODE_NIGHT_YES -> THEME_DARK
            else -> THEME_SYSTEM
        }
    }

    /**
     * Sets, persists, and applies the theme mode.
     */
    fun setTheme(themeName: String) {
        val mode = getNightMode(themeName)
        Log.i(TAG, "Setting theme to: $themeName (mode: $mode)")
        sharedPreferences.edit().putString(KEY_THEME_MODE, themeName).apply()
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    /**
     * Gets the currently selected theme name.
     */
    fun getCurrentThemeName(): String {
        val name = sharedPreferences.getString(KEY_THEME_MODE, THEME_SYSTEM) ?: THEME_SYSTEM
        Log.d(TAG, "Retrieved current theme name: $name")
        return name
    }

    /**
     * Applies the persisted theme on app startup.
     */
    fun applyPersistedTheme() {
        val name = getCurrentThemeName()
        val mode = getNightMode(name)
        Log.i(TAG, "Applying persisted theme: $name (mode: $mode)")
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
