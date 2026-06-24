package com.media.iptvplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class DualPlayerActivity : AppCompatActivity() {

    private lateinit var player1: ExoPlayer
    private lateinit var player2: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {

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

        val url1 =
            intent.getStringExtra("url1")
                ?: ""

        val url2 =
            intent.getStringExtra("url2")
                ?: ""

        player1 =
            ExoPlayer.Builder(this).build()

        player2 =
            ExoPlayer.Builder(this).build()

        playerView1.player = player1
        playerView2.player = player2

        if (url1.isNotEmpty()) {

            player1.setMediaItem(
                MediaItem.fromUri(
                    Uri.parse(url1)
                )
            )

            player1.prepare()
            player1.play()
        }

        if (url2.isNotEmpty()) {

            player2.setMediaItem(
                MediaItem.fromUri(
                    Uri.parse(url2)
                )
            )

            player2.prepare()
            player2.play()
        }
    }

    override fun onDestroy() {

        super.onDestroy()

        if (::player1.isInitialized)
            player1.release()

        if (::player2.isInitialized)
            player2.release()
    }
}
