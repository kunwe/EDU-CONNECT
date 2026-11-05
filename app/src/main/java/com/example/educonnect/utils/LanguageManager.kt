package com.example.educonnect.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages in-app language switching and persistence using AndroidX AppCompatDelegate per-app locales
 * combined with robust SharedPreferences storage for universal device compatibility.
 */
@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "LanguageManager"
        private const val PREF_NAME = "educonnect_lang_prefs"
        private const val KEY_LANG_TAG = "lang_tag"

        const val LANG_ENGLISH = "English"
        const val LANG_ZULU = "isiZulu"
        const val LANG_AFRIKAANS = "Afrikaans"
        const val LANG_SESOTHO = "Sesotho"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Maps language display names to language tags.
     */
    fun getLanguageTag(languageName: String): String {
        return when (languageName) {
            LANG_ZULU -> "zu"
            LANG_AFRIKAANS -> "af"
            LANG_SESOTHO -> "st"
            else -> "en"
        }
    }

    /**
     * Maps language tags to display names.
     */
    fun getLanguageName(languageTag: String): String {
        return when (languageTag) {
            "zu" -> LANG_ZULU
            "af" -> LANG_AFRIKAANS
            "st" -> LANG_SESOTHO
            else -> LANG_ENGLISH
        }
    }

    /**
     * Sets and persists the application language, updating app locales via AppCompatDelegate and SharedPreferences.
     */
    fun setLanguage(languageName: String) {
        val tag = getLanguageTag(languageName)
        Log.i(TAG, "Setting application language to: $languageName (tag: $tag)")
        sharedPreferences.edit().putString(KEY_LANG_TAG, tag).apply()

        val appLocales = LocaleListCompat.forLanguageTags(tag)
        AppCompatDelegate.setApplicationLocales(appLocales)
    }

    /**
     * Gets the currently active application language display name.
     */
    fun getCurrentLanguageName(): String {
        val savedTag = sharedPreferences.getString(KEY_LANG_TAG, null)
        if (!savedTag.isNullOrBlank()) {
            val name = getLanguageName(savedTag)
            Log.d(TAG, "Retrieved language from SharedPreferences: $savedTag, name: $name")
            return name
        }
        val locales = AppCompatDelegate.getApplicationLocales()
        if (!locales.isEmpty) {
            val tag = locales.get(0)?.language ?: "en"
            val name = getLanguageName(tag)
            Log.d(TAG, "Current language tag: $tag, resolved name: $name")
            return name
        }
        return LANG_ENGLISH
    }

    /**
     * Gets prompt instruction suffix for AI / Gemini requests in the current language.
     */
    fun getAiPromptInstruction(): String {
        val currentLang = getCurrentLanguageName()
        return "Respond in $currentLang. Keep subject terminology accurate and CAPS-aligned. Keep JSON/quiz structure keys in English."
    }
}
