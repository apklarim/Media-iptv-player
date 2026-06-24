package com.media.iptvplayer

import android.content.Intent
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

        if (channels.isEmpty()) {

            val demo = mutableListOf<String>()

            for (i in 1..20) {
                demo.add("Test Kanal $i")
            }

            listView.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                demo
            )

            return
        }

        val names = channels.map { it.name }

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            names
        )

        listView.setOnItemClickListener { _, _, position, _ ->

            val selectedChannel = channels[position]

            startActivity(
                Intent(
                    this,
                    PlayerActivity::class.java
                ).apply {

                    putExtra(
                        "url",
                        selectedChannel.url
                    )
                }
            )
        }
    }
}
