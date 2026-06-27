package com.media.iptvplayer

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.media.iptvplayer.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistListActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playlist_list)

        val btnAdd =
            findViewById<Button>(
                R.id.btnAddPlaylist
            )

        listView =
            findViewById(
                R.id.listPlaylists
            )

        btnAdd.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AddPlaylistActivity::class.java
                )
            )
        }

        loadPlaylists()
    }

    override fun onResume() {
        super.onResume()
        loadPlaylists()
    }

    private fun loadPlaylists() {

        val playlists =
            PlaylistManager.getPlaylists(this)

        val names =
            playlists.map {
                "${it.name} (${it.type})"
            }

        listView.adapter =
            PlaylistAdapter(
                this,
                names
            )

        listView.setOnItemClickListener {
                _, _, position, _ ->

            val playlist = playlists[position]

            LastPlaylistManager.saveLastPlaylist(
                this,
                playlist.id
            )

            lifecycleScope.launch {

                try {

                    Toast.makeText(
                        this@PlaylistListActivity,
                        "Liste yükleniyor...",
                        Toast.LENGTH_SHORT
                    ).show()

                    var content = ""

                    if (playlist.type == "M3U_FILE") {

                        content =
                            withContext(
                                Dispatchers.IO
                            ) {

                                contentResolver
                                    .openInputStream(
                                        Uri.parse(
                                            playlist.url
                                        )
                                    )
                                    ?.bufferedReader()
                                    ?.use {
                                        it.readText()
                                    } ?: ""
                            }

                    } else {

                        content =
                            PlaylistCacheManager
                                .getCache(
                                    this@PlaylistListActivity,
                                    playlist.url
                                ) ?: ""

                        if (content.isEmpty()) {

                            content =
                                withContext(
                                    Dispatchers.IO
                                ) {

                                    NetworkUtils
                                        .downloadText(
                                            playlist.url
                                        )
                                }

                            PlaylistCacheManager
                                .saveCache(
                                    this@PlaylistListActivity,
                                    playlist.url,
                                    content
                                )
                        }
                    }

                    ChannelRepository.setChannels(
                        M3uParser.parse(content)
                    )

                    Toast.makeText(
                        this@PlaylistListActivity,
                        "Liste yüklendi",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(
                            this@PlaylistListActivity,
                            MainActivity::class.java
                        )
                    )

                } catch (e: Exception) {

                    Toast.makeText(
                        this@PlaylistListActivity,
                        "Hata: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        listView.setOnItemLongClickListener {
                _, _, position, _ ->

            val playlist = playlists[position]

            AlertDialog.Builder(this)
                .setTitle(
                    playlist.name
                )

                .setItems(
                    arrayOf(
                        "Düzenle",
                        "Sil",
                        "İptal"
                    )
                ) { dialog, which ->

                    when (which) {

                        0 -> {

                            val editText =
                                EditText(this)

                            editText.setText(
                                playlist.name
                            )

                            AlertDialog.Builder(this)
                                .setTitle(
                                    "Liste Adını Düzenle"
                                )

                                .setView(editText)

                                .setPositiveButton(
                                    "Kaydet"
                                ) { _, _ ->

                                    val newName =
                                        editText.text
                                            .toString()
                                            .trim()

                                    if (newName.isNotEmpty()) {

                                        val allLists =
                                            PlaylistManager
                                                .getPlaylists(
                                                    this
                                                )

                                        val index =
                                            allLists
                                                .indexOfFirst {
                                                    it.id ==
                                                            playlist.id
                                                }

                                        if (index != -1) {

                                            allLists[index] =
                                                Playlist(
                                                    id = playlist.id,
                                                    name = newName,
                                                    type = playlist.type,
                                                    url = playlist.url,
                                                    username = playlist.username,
                                                    password = playlist.password,
                                                    server = playlist.server
                                                )

                                            PlaylistManager
                                                .savePlaylists(
                                                    this,
                                                    allLists
                                                )

                                            Toast.makeText(
                                                this,
                                                "Liste güncellendi",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            loadPlaylists()
                                        }
                                    }
                                }

                                .setNegativeButton(
                                    "İptal",
                                    null
                                )

                                .show()
                        }

                        1 -> {

                            PlaylistManager
                                .deletePlaylist(
                                    this,
                                    playlist.id
                                )

                            PlaylistCacheManager
                                .clearCache(this)

                            Toast.makeText(
                                this,
                                "Liste silindi",
                                Toast.LENGTH_SHORT
                            ).show()

                            loadPlaylists()
                        }
                    }

                    dialog.dismiss()
                }

                .show()

            true
        }
    }
}
