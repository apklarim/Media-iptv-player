package com.media.iptvplayer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChannelListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_channel_list)

        val title =
            findViewById<TextView>(R.id.tvPlaylistName)

        val listView =
            findViewById<ListView>(R.id.listChannels)

        title.text =
            intent.getStringExtra("playlistName")
                ?: "Kanal Listesi"

        val names =
            ChannelRepository.channels.map { it.name }

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            names
        )
    }
}
