package com.media.iptvplayer

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class DualPlayerActivity : AppCompatActivity() {

    private lateinit var player1: ExoPlayer
    private lateinit var player2: ExoPlayer

    private lateinit var list1: ListView
    private lateinit var list2: ListView

    private val channels =
        ChannelRepository.channels

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_dual_player
        )

        val playerView1 =
            findViewById<PlayerView>(
                R.id.playerView1
            )

        val playerView2 =
            findViewById<PlayerView>(
                R.id.playerView2
            )

        list1 =
            findViewById(
                R.id.listChannels1
            )

        list2 =
            findViewById(
                R.id.listChannels2
            )

        player1 =
            ExoPlayer.Builder(this)
                .build()

        player2 =
            ExoPlayer.Builder(this)
                .build()

        playerView1.player = player1
        playerView2.player = player2

        val url1 =
            intent.getStringExtra(
                "url1"
            )

        val url2 =
            intent.getStringExtra(
                "url2"
            )

        if (!url1.isNullOrEmpty()) {

            playOnPlayer1(url1)
        }

        if (!url2.isNullOrEmpty()) {

            playOnPlayer2(url2)
        }

        loadLists()
    }

    private fun loadLists() {

        val names =
            channels.map {
                it.name
            }

        list1.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                names
            )

        list2.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                names
            )

        list1.setOnItemClickListener {
                _, _, position, _ ->

            playOnPlayer1(
                channels[position].url
            )
        }

        list2.setOnItemClickListener {
                _, _, position, _ ->

            playOnPlayer2(
                channels[position].url
            )
        }
    }

    private fun playOnPlayer1(
        url: String
    ) {

        player1.stop()

        player1.setMediaItem(
            MediaItem.fromUri(
                Uri.parse(url)
            )
        )

        player1.prepare()
        player1.playWhenReady = true
    }

    private fun playOnPlayer2(
        url: String
    ) {

        player2.stop()

        player2.setMediaItem(
            MediaItem.fromUri(
                Uri.parse(url)
            )
        )

        player2.prepare()
        player2.playWhenReady = true
    }

    override fun onDestroy() {

        super.onDestroy()

        player1.release()
        player2.release()
    }
}
