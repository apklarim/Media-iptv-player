package com.media.iptvplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.playerView)

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

        playerView.setShowNextButton(false)
        playerView.setShowPreviousButton(false)
        playerView.setShowRewindButton(true)
        playerView.setShowFastForwardButton(true)

        val mediaItem = MediaItem.fromUri(
            Uri.parse(url)
        )

        player.setMediaItem(mediaItem)

        player.prepare()

        player.playWhenReady = true
    }

    override fun onStart() {
        super.onStart()

        if (::player.isInitialized) {
            player.play()
        }
    }

    override fun onStop() {
        super.onStop()

        if (::player.isInitialized) {
            player.pause()
        }
    }

    override fun onDestroy() {

        super.onDestroy()

        if (::player.isInitialized) {
            player.release()
        }
    }
}
