package com.media.iptvplayer

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
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

            Toast.makeText(
                this,
                "URL bulunamadı",
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        Toast.makeText(
            this,
            "URL alındı",
            Toast.LENGTH_SHORT
        ).show()

        player = ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build()

        playerView.player = player

        player.addListener(object : Player.Listener {

            override fun onPlayerError(
                error: PlaybackException
            ) {

                Toast.makeText(
                    this@PlayerActivity,
                    "Player Hatası: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        val mediaItem = MediaItem.fromUri(
            Uri.parse(url)
        )

        player.setMediaItem(mediaItem)

        player.prepare()

        player.playWhenReady = true
    }

    override fun onDestroy() {

        super.onDestroy()

        if (::player.isInitialized) {
            player.release()
        }
    }
}
