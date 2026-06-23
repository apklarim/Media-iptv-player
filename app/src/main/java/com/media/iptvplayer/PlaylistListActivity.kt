package com.media.iptvplayer

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PlaylistListActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playlist_list)

        listView = findViewById(R.id.listPlaylists)

        loadPlaylists()
    }

    private fun loadPlaylists() {

        val playlists =
            PlaylistManager.getPlaylists(this)

        val names =
            playlists.map { "${it.name} (${it.type})" }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            names
        )

        listView.adapter = adapter

        // Tıklama

        listView.setOnItemClickListener { _, _, position, _ ->

            Toast.makeText(
                this,
                playlists[position].name,
                Toast.LENGTH_SHORT
            ).show()
        }

        // Uzun basma

        listView.setOnItemLongClickListener { _, _, position, _ ->

            AlertDialog.Builder(this)
                .setTitle(playlists[position].name)
                .setItems(
                    arrayOf(
                        "Düzenle",
                        "Sil"
                    )
                ) { _, which ->

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
                                playlists[position].id
                            )

                            Toast.makeText(
                                this,
                                "Liste silindi",
                                Toast.LENGTH_SHORT
                            ).show()

                            loadPlaylists()
                        }
                    }
                }
                .show()

            true
        }
    }
}
