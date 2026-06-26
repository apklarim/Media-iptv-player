package com.media.iptvplayer

import android.app.PictureInPictureParams
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

class PlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var channelList: ListView

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

        val btnPrev =
            findViewById<Button>(
                R.id.btnPrev
            )

        val btnNext =
            findViewById<Button>(
                R.id.btnNext
            )

        val btnDual =
            findViewById<Button>(
                R.id.btnDual
            )

        val url =
            intent.getStringExtra(
                "url"
            )

        currentIndex =
            channels.indexOfFirst {
                it.url == url
            }

        if (currentIndex < 0)
            currentIndex = 0

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

        // 2X Dual Player

        btnDual.setOnClickListener {

            if (channels.size < 2)
                return@setOnClickListener

            val secondIndex =
                if (
                    currentIndex + 1 <
                    channels.size
                )
                    currentIndex + 1
                else
                    0

            startActivity(

                Intent(
                    this,
                    DualPlayerActivity::class.java
                )
                    .putExtra(
                        "url1",
                        channels[currentIndex].url
                    )
                    .putExtra(
                        "url2",
                        channels[secondIndex].url
                    )
            )
        }

        loadChannelList()
    }

    private fun playChannel(
        index: Int
    ) {

        if (
            index < 0 ||
            index >= channels.size
        ) return

        currentIndex = index

        val channel =
            channels[index]

        PlayerPreferences
            .saveLastChannel(
                this,
                channel.url
            )

        val mediaItem =
            MediaItem.fromUri(
                Uri.parse(
                    channel.url.trim()
                )
            )

        player.setMediaItem(
            mediaItem
        )

        player.prepare()
        player.playWhenReady = true
    }

    override fun onUserLeaveHint() {

        super.onUserLeaveHint()

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val params =
                PictureInPictureParams
                    .Builder()
                    .setAspectRatio(
                        Rational(16, 9)
                    )
                    .build()

            enterPictureInPictureMode(
                params
            )
        }
    }

    private fun previousChannel() {

        if (currentIndex > 0) {

            currentIndex--

            playChannel(currentIndex)
        }
    }

    private fun nextChannel() {

        if (
            currentIndex <
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
                android.R.layout.simple_list_item_1,
                channels.map {
                    it.name
                }
            )

        channelList.setOnItemClickListener {
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
