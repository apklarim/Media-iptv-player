package com.media.iptvplayer

import android.content.Context

object SettingsPreferences {

    private const val PREFS =
        "media_iptv_settings"

    private const val AUTO_HIDE =
        "auto_hide"

    fun setAutoHideEnabled(
        context: Context,
        enabled: Boolean
    ) {

        context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        )
            .edit()
            .putBoolean(
                AUTO_HIDE,
                enabled
            )
            .apply()
    }

    fun isAutoHideEnabled(
        context: Context
    ): Boolean {

        return context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .getBoolean(
                AUTO_HIDE,
                true
            )
    }
}
