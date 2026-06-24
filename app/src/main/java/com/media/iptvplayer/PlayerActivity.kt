package com.media.iptvplayer

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
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

    private var channels = ChannelRepository.channels

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.playerView)
        channelList = findViewById(R.id.listChannels)

        val url = intent.getStringExtra("url")

        if (url.isNullOrEmpty()) {
            finish()
            return
        }

        player = ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build()

        playerView.player = player

        playerView.resizeMode =
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        val mediaItem =
            MediaItem.fromUri(Uri.parse(url))

        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true

        loadChannelList()
    }

    private fun loadChannelList() {

        channelList.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                channels.map { it.name }
            )

        channelList.setOnItemClickListener { _, _, position, _ ->

            val mediaItem =
                MediaItem.fromUri(
                    Uri.parse(channels[position].url)
                )

            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()

            channelList.visibility = View.GONE
        }
    }

    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean {

        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
            keyCode == KeyEvent.KEYCODE_ENTER) {

            channelList.visibility =
                if (channelList.visibility == View.VISIBLE)
                    View.GONE
                else
                    View.VISIBLE

            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {

        super.onDestroy()

        if (::player.isInitialized) {
            player.release()
        }
    }
}
