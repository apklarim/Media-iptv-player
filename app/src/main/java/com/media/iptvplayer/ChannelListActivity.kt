package com.media.iptvplayer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChannelListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_channel_list)

        val listView =
            findViewById<ListView>(R.id.listChannels)

        val channels = ChannelRepository.channels

        Toast.makeText(
            this,
            "Toplam kanal: ${channels.size}",
            Toast.LENGTH_LONG
        ).show()

        val names = channels.map { it.name }

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            names
        )
    }
}
