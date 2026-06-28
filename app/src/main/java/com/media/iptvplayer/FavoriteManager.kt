package com.media.iptvplayer

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FavoriteManager {

    private const val FILE_NAME =
        "favorites.json"

    private fun getFavorites():
            MutableSet<String> {

        val json =
            FileStorageManager.readText(
                FILE_NAME,
                "[]"
            )

        val type =
            object : TypeToken<MutableSet<String>>() {}.type

        return try {

            Gson().fromJson<MutableSet<String>>(
                json,
                type
            ) ?: mutableSetOf()

        } catch (e: Exception) {

            mutableSetOf()
        }
    }

    private fun saveFavorites(
        favorites: MutableSet<String>
    ) {

        FileStorageManager.writeText(
            FILE_NAME,
            Gson().toJson(favorites)
        )
    }

    fun isFavorite(
        context: Context,
        channelName: String
    ): Boolean {

        return getFavorites()
            .contains(channelName)
    }

    fun toggleFavorite(
        context: Context,
        channelName: String
    ) {

        val favorites =
            getFavorites()

        if (favorites.contains(channelName)) {

            favorites.remove(channelName)

        } else {

            favorites.add(channelName)
        }

        saveFavorites(favorites)
    }
}
