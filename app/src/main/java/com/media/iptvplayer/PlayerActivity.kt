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

        setContentView(
            R.layout.activity_player
        )

        playerView =
            findViewById(R.id.playerView)

        val url =
            intent.getStringExtra("url")
                ?: return

        player =
            ExoPlayer.Builder(this)
                .build()

        playerView.player = player

        val mediaItem =
            MediaItem.fromUri(
                Uri.parse(url)
            )

        player.setMediaItem(mediaItem)

        player.prepare()

        player.play()

        // Film ve diziler için
        // süre çubuğu + ileri/geri aktif

        playerView.setShowNextButton(false)
        playerView.setShowPreviousButton(false)
        playerView.setShowRewindButton(true)
        playerView.setShowFastForwardButton(true)

        // 10 saniye geri
        player.seekBackIncrement = 10000

        // 10 saniye ileri
        player.seekForwardIncrement = 10000
    }

    override fun onStart() {
        super.onStart()
        player.play()
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroy() {

        super.onDestroy()

        player.release()
    }
}
