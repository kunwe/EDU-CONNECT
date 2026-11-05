package com.example.educonnect

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.educonnect.utils.ThemeManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Unit tests for [ThemeManager].
 */
class ThemeManagerTest {

    private lateinit var context: Context
    private lateinit var themeManager: ThemeManager

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        themeManager = ThemeManager(context)
    }

    @Test
    fun testGetNightMode() {
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, themeManager.getNightMode(ThemeManager.THEME_LIGHT))
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, themeManager.getNightMode(ThemeManager.THEME_DARK))
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, themeManager.getNightMode(ThemeManager.THEME_SYSTEM))
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, themeManager.getNightMode("Invalid"))
    }

    @Test
    fun testGetThemeName() {
        assertEquals(ThemeManager.THEME_LIGHT, themeManager.getThemeName(AppCompatDelegate.MODE_NIGHT_NO))
        assertEquals(ThemeManager.THEME_DARK, themeManager.getThemeName(AppCompatDelegate.MODE_NIGHT_YES))
        assertEquals(ThemeManager.THEME_SYSTEM, themeManager.getThemeName(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM))
    }
}
