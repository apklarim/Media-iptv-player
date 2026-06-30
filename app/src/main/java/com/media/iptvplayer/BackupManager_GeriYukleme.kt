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

            true

        } catch (e: Exception) {

            e.printStackTrace()
            false
        }
    }

    fun restoreBackup(context: Context): Boolean {

        return try {

            val file = File(
                context.getExternalFilesDir(null),
                "MEDYA_IPTV_BACKUP.medyaiptvbackup"
            )

            if (!file.exists())
                return false

            val backup = Gson().fromJson(
                file.readText(),
                BackupData::class.java
            )

            context.getSharedPreferences(
                "playlists",
                Context.MODE_PRIVATE
            ).edit()
                .putString(
                    "playlist_list",
                    backup.playlistsJson
                )
                .apply()

            context.getSharedPreferences(
                "favorites",
                Context.MODE_PRIVATE
            ).edit()
                .putStringSet(
                    "favorite_channels",
                    backup.favorites
                )
                .apply()

            HiddenGroupsManager.clearHiddenGroups(context)

            backup.hiddenGroups.forEach {
                HiddenGroupsManager.hideGroup(
                    context,
                    it
                )
            }

            Toast.makeText(
                context,
                "Yedek geri yüklendi",
                Toast.LENGTH_LONG
            ).show()

            true

        } catch (e: Exception) {

            e.printStackTrace()
            false
        }
    }
}
