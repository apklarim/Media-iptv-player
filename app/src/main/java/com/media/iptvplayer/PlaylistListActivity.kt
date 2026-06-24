package com.media.iptvplayer

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playlist_list)

        val btnAdd =
            findViewById<Button>(R.id.btnAddPlaylist)

        val listView =
            findViewById<ListView>(R.id.listPlaylists)

        btnAdd.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AddPlaylistActivity::class.java
                )
            )
        }

        val playlists =
            PlaylistManager.getPlaylists(this)

        val names =
            playlists.map {
                "${it.name} (${it.type})"
            }

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            names
        )

        listView.setOnItemClickListener { _, _, position, _ ->

            val playlist = playlists[position]

            if (playlist.type == "M3U_FILE") {

                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )

                return@setOnItemClickListener
            }

            lifecycleScope.launch {

                try {

                    Toast.makeText(
                        this@PlaylistListActivity,
                        "Liste yükleniyor...",
                        Toast.LENGTH_SHORT
                    ).show()

                    val content =
                        withContext(Dispatchers.IO) {

                            NetworkUtils.downloadText(
                                playlist.url
                            )
                        }

                    ChannelRepository.channels =
                        M3uParser.parse(content)

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

        // Uzun basınca menü

        listView.setOnItemLongClickListener { _, _, position, _ ->

            val playlist = playlists[position]

            AlertDialog.Builder(this)
                .setTitle(playlist.name)
                .setItems(
                    arrayOf(
                        "Düzenle",
                        "Sil",
                        "İptal"
                    )
                ) { dialog, which ->

                    when (which) {

                        0 -> {

                            Toast.makeText(
                                this,
                                "Düzenleme yakında eklenecek",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        1 -> {

                            PlaylistManager.deletePlaylist(
                                this,
                                playlist.id
                            )

                            recreate()
                        }
                    }

                    dialog.dismiss()
                }
                .show()

            true
        }
    }
}
