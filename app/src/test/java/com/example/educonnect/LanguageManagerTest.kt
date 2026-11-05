package com.example.educonnect

import android.content.Context
import com.example.educonnect.utils.LanguageManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Unit tests for [LanguageManager].
 */
class LanguageManagerTest {

    private lateinit var context: Context
    private lateinit var languageManager: LanguageManager

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        languageManager = LanguageManager(context)
    }

    @Test
    fun testGetLanguageTag() {
        assertEquals("zu", languageManager.getLanguageTag(LanguageManager.LANG_ZULU))
        assertEquals("af", languageManager.getLanguageTag(LanguageManager.LANG_AFRIKAANS))
        assertEquals("st", languageManager.getLanguageTag(LanguageManager.LANG_SESOTHO))
        assertEquals("en", languageManager.getLanguageTag(LanguageManager.LANG_ENGLISH))
        assertEquals("en", languageManager.getLanguageTag("Unknown"))
    }

    @Test
    fun testGetLanguageName() {
        assertEquals(LanguageManager.LANG_ZULU, languageManager.getLanguageName("zu"))
        assertEquals(LanguageManager.LANG_AFRIKAANS, languageManager.getLanguageName("af"))
        assertEquals(LanguageManager.LANG_SESOTHO, languageManager.getLanguageName("st"))
        assertEquals(LanguageManager.LANG_ENGLISH, languageManager.getLanguageName("en"))
        assertEquals(LanguageManager.LANG_ENGLISH, languageManager.getLanguageName("fr"))
    }
}
