package com.media.iptvplayer

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

class PlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var channelList: ListView
    private lateinit var txtChannelName: TextView

    private var channels =
        ChannelRepository.channels

    private var currentIndex = 0

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_player
        )

        playerView =
            findViewById(
                R.id.playerView
            )

        channelList =
            findViewById(
                R.id.listChannels
            )

        txtChannelName =
            findViewById(
                R.id.txtChannelName
            )

        val btnPrev =
            findViewById<Button>(
                R.id.btnPrev
            )

        val btnNext =
            findViewById<Button>(
                R.id.btnNext
            )

        val url =
            intent.getStringExtra(
                "url"
            )

        currentIndex =
            channels.indexOfFirst {
                it.url == url
            }

        if (currentIndex < 0) {

            val lastUrl =
                PlayerPreferences
                    .getLastChannel(this)

            currentIndex =
                channels.indexOfFirst {
                    it.url == lastUrl
                }

            if (currentIndex < 0)
                currentIndex = 0
        }

        player =
            ExoPlayer.Builder(this)
                .build()

        playerView.player = player

        playerView.resizeMode =
            AspectRatioFrameLayout
                .RESIZE_MODE_ZOOM

        playChannel(currentIndex)

        btnPrev.setOnClickListener {

            previousChannel()
        }

        btnNext.setOnClickListener {

            nextChannel()
        }

        loadChannelList()
    }

    private fun playChannel(
        index: Int
    ) {

        if (index < 0 ||
            index >= channels.size
        ) return

        currentIndex = index

        val channel =
            channels[index]

        txtChannelName.text =
            channel.name

        PlayerPreferences
            .saveLastChannel(
                this,
                channel.url
            )

        val mediaItem =
            MediaItem.fromUri(
                Uri.parse(channel.url)
            )

        player.setMediaItem(
            mediaItem
        )

        player.prepare()

        player.playWhenReady = true
    }

    private fun previousChannel() {

        if (currentIndex > 0) {

            currentIndex--

            playChannel(currentIndex)
        }
    }

    private fun nextChannel() {

        if (currentIndex <
            channels.size - 1
        ) {

            currentIndex++

            playChannel(currentIndex)
        }
    }

    private fun loadChannelList() {

        channelList.adapter =
            ArrayAdapter(
                this,
                android.R.layout
                    .simple_list_item_1,

                channels.map {
                    it.name
                }
            )

        channelList
            .setOnItemClickListener {
                    _, _, position, _ ->

                playChannel(position)

                channelList.visibility =
                    View.GONE
            }
    }

    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean {

        when (keyCode) {

            KeyEvent.KEYCODE_DPAD_UP -> {

                previousChannel()
                return true
            }

            KeyEvent.KEYCODE_DPAD_DOWN -> {

                nextChannel()
                return true
            }

            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_ENTER -> {

                channelList.visibility =
                    if (
                        channelList.visibility ==
                        View.VISIBLE
                    )
                        View.GONE
                    else
                        View.VISIBLE

                return true
            }
        }

        return super.onKeyDown(
            keyCode,
            event
        )
    }

    override fun onDestroy() {

        super.onDestroy()

        if (::player.isInitialized) {

            player.release()
        }
    }
}
