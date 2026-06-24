package com.media.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.media.iptvplayer.model.Channel

class ChannelListActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    private var channels =
        mutableListOf<Channel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_channel_list
        )

        listView =
            findViewById(R.id.listChannels)

        val category =
            intent.getStringExtra(
                "CATEGORY"
            ) ?: "LIVE"

        channels =
            ChannelRepository.channels
                .filter {

                    it.category == category

                }
                .toMutableList()

        channels.forEach {

            it.isFavorite =
                FavoriteManager.isFavorite(
                    this,
                    it.name
                )
        }

        channels.sortByDescending {
            it.isFavorite
        }

        loadList()

        listView.setOnItemClickListener {

                _, _, position, _ ->

            startActivity(

                Intent(
                    this,
                    PlayerActivity::class.java
                )

                    .putExtra(
                        "url",
                        channels[position].url
                    )
            )
        }

        listView.setOnItemLongClickListener {

                _, _, position, _ ->

            FavoriteManager.toggleFavorite(
                this,
                channels[position].name
            )

            channels[position].isFavorite =
                !channels[position].isFavorite

            channels.sortByDescending {
                it.isFavorite
            }

            loadList()

            true
        }
    }

    private fun loadList() {

        val names =
            channels.map {

                if (it.isFavorite)

                    "⭐ ${it.name}"

                else

                    it.name
            }

        listView.adapter =
            ArrayAdapter(
                this,
                android.R.layout
                    .simple_list_item_1,
                names
            )
    }
}
