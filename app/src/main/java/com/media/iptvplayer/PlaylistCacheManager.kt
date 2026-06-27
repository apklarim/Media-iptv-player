package com.media.iptvplayer

import android.content.Context

object PlaylistCacheManager {

    private const val PREFS =
        "playlist_cache"

    fun saveCache(
        context: Context,
        url: String,
        content: String
    ) {

        context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(url, content)
            .apply()
    }

    fun getCache(
        context: Context,
        url: String
    ): String? {

        return context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .getString(url, null)
    }

    fun clearCache(
        context: Context
    ) {

        context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .edit()
            .clear()
            .apply()
    }
}
