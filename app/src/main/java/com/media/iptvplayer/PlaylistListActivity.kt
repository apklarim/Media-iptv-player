package com.media.iptvplayer

import android.app.AlertDialog
import android.content.Intent
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

    override fun onResume() {
        super.onResume()
        loadPlaylists()
    }

    private fun loadPlaylists() {

        val playlists =
            PlaylistManager.getPlaylists(this)

        val names =
            playlists.map { "${it.name} (${it.type})" }

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            names
        )

        // Normal tıklama

        listView.setOnItemClickListener { _, _, position, _ ->

            val intent =
                Intent(
                    this,
                    ChannelListActivity::class.java
                )

            intent.putExtra(
                "playlistName",
                playlists[position].name
            )

            startActivity(intent)
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

                            loadPlaylists()
                        }
                    }
                }
                .show()

            true
        }
    }
}
