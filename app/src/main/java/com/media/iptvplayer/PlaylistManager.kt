package com.media.iptvplayer

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.media.iptvplayer.model.Playlist

object PlaylistManager {

    private const val FILE_NAME =
        "playlists.json"

    fun getPlaylists(
        context: Context
    ): MutableList<Playlist> {

        val json =
            FileStorageManager.readText(
                FILE_NAME,
                "[]"
            )

        val type =
            object : TypeToken<MutableList<Playlist>>() {}.type

        return try {

            Gson().fromJson<MutableList<Playlist>>(
                json,
                type
            ) ?: mutableListOf()

        } catch (e: Exception) {

            mutableListOf()
        }
    }

    fun savePlaylists(
        context: Context,
        playlists: MutableList<Playlist>
    ) {

        FileStorageManager.writeText(
            FILE_NAME,
            Gson().toJson(playlists)
        )
    }

    fun addPlaylist(
        context: Context,
        playlist: Playlist
    ) {

        val list =
            getPlaylists(context)

        list.add(playlist)

        savePlaylists(
            context,
            list
        )
    }

    fun deletePlaylist(
        context: Context,
        id: Long
    ) {

        val list =
            getPlaylists(context)

        list.removeAll {
            it.id == id
        }

        savePlaylists(
            context,
            list
        )
    }
}
