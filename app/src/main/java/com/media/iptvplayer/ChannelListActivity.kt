package com.media.iptvplayer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
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

        if (ChannelRepository.channels.isEmpty()) {

            Toast.makeText(
                this,
                "Hiç kanal bulunamadı",
                Toast.LENGTH_LONG
            ).show()

            listView.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                listOf(
                    "Kanal bulunamadı",
                    "M3U URL kontrol edin"
                )
            )

            return
        }

        Toast.makeText(
            this,
            "Toplam kanal: ${ChannelRepository.channels.size}",
            Toast.LENGTH_LONG
        ).show()

        val channelNames =
            ChannelRepository.channels.map { it.name }

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            channelNames
        )
    }
}
