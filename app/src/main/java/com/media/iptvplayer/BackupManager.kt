package com.media.iptvplayer

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import java.io.File

data class BackupData(
    val playlistsJson: String,
    val favorites: Set<String>,
    val hiddenGroups: Set<String>
)

object BackupManager {

    fun createBackup(context: Context): Boolean {

        return try {

            val playlistsJson =
                context.getSharedPreferences(
                    "playlists",
                    Context.MODE_PRIVATE
                ).getString(
                    "playlist_list",
                    "[]"
                ) ?: "[]"

            val favorites =
                context.getSharedPreferences(
                    "favorites",
                    Context.MODE_PRIVATE
                ).getStringSet(
                    "favorite_channels",
                    emptySet()
                ) ?: emptySet()

            val hiddenGroups =
                HiddenGroupsManager
                    .getHiddenGroups(context)

            val backup =
                BackupData(
                    playlistsJson,
                    favorites,
                    hiddenGroups
                )

            val file = File(
                context.getExternalFilesDir(null),
                "MEDYA_IPTV_BACKUP.medyaiptvbackup"
            )

            file.writeText(
                Gson().toJson(backup)
            )

            Toast.makeText(
                context,
                "Yedek oluşturuldu",
                Toast.LENGTH_LONG
            ).show()

            true

        } catch (e: Exception) {

            e.printStackTrace()
            false
        }
    }
}
