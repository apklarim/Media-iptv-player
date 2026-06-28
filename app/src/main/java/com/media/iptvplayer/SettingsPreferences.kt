package com.media.iptvplayer

import android.content.Context
import com.google.gson.Gson

object SettingsPreferences {

    private const val FILE_NAME =
        "settings.json"

    private data class SettingsData(

        var autoHide: Boolean = true,

        var autoLoadLastPlaylist: Boolean = true,

        var autoLoadLastChannel: Boolean = true
    )

    private fun loadSettings():
            SettingsData {

        return try {

            val json =
                FileStorageManager.readText(
                    FILE_NAME,
                    ""
                )

            if (json.isEmpty()) {

                SettingsData()

            } else {

                Gson().fromJson(
                    json,
                    SettingsData::class.java
                ) ?: SettingsData()
            }

        } catch (e: Exception) {

            SettingsData()
        }
    }

    private fun saveSettings(
        settings: SettingsData
    ) {

        FileStorageManager.writeText(
            FILE_NAME,
            Gson().toJson(settings)
        )
    }

    fun setAutoHideEnabled(
        context: Context,
        enabled: Boolean
    ) {

        val settings =
            loadSettings()

        settings.autoHide = enabled

        saveSettings(settings)
    }

    fun isAutoHideEnabled(
        context: Context
    ): Boolean {

        return loadSettings().autoHide
    }

    fun setAutoLoadLastPlaylistEnabled(
        context: Context,
        enabled: Boolean
    ) {

        val settings =
            loadSettings()

        settings.autoLoadLastPlaylist =
            enabled

        saveSettings(settings)
    }

    fun isAutoLoadLastPlaylistEnabled(
        context: Context
    ): Boolean {

        return loadSettings()
            .autoLoadLastPlaylist
    }

    fun setAutoLoadLastChannelEnabled(
        context: Context,
        enabled: Boolean
    ) {

        val settings =
            loadSettings()

        settings.autoLoadLastChannel =
            enabled

        saveSettings(settings)
    }

    fun isAutoLoadLastChannelEnabled(
        context: Context
    ): Boolean {

        return loadSettings()
            .autoLoadLastChannel
    }
}
