package com.media.iptvplayer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class PlaylistListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playlist_list)

        val listView =
            findViewById<ListView>(R.id.listPlaylists)

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
    }
}
