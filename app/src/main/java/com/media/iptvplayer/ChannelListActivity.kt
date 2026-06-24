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

        // Kanal yoksa test kanallarını göster
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

            Toast.makeText(
                this,
                "Gerçek kanal bulunamadı, test kanalları gösteriliyor",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        // Gerçek kanalları göster
        val names = channels.map { it.name }

        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            names
        )

        listView.setOnItemClickListener { _, _, position, _ ->

            val selectedChannel = channels[position]

            Toast.makeText(
                this,
                "Seçilen kanal: ${selectedChannel.name}",
                Toast.LENGTH_SHORT
            ).show()

            // Daha sonra burada PlayerActivity açılacak
        }
    }
}
