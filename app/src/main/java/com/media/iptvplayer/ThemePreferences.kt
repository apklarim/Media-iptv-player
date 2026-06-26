package com.media.iptvplayer

import android.content.Context

object ThemePreferences {

    private const val PREFS =
        "theme_preferences"

    private const val KEY_THEME =
        "selected_theme"

    const val THEME_DARK = "dark"
    const val THEME_TURQUOISE = "turquoise"
    const val THEME_BLUE = "blue"

    fun saveTheme(
        context: Context,
        theme: String
    ) {

        context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(
                KEY_THEME,
                theme
            )
            .apply()
    }

    fun getTheme(
        context: Context
    ): String {

        return context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .getString(
                KEY_THEME,
                THEME_DARK
            ) ?: THEME_DARK
    }
}
