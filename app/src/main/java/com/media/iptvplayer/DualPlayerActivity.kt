package com.media.iptvplayer

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

class DualPlayerActivity : AppCompatActivity() {

    private lateinit var player1: ExoPlayer
    private lateinit var player2: ExoPlayer

    private lateinit var playerView1: PlayerView
    private lateinit var playerView2: PlayerView

    private lateinit var list1: ListView
    private lateinit var list2: ListView

    private lateinit var rootLayout: LinearLayout

    private val channels =
        ChannelRepository.channels

    private val handler =
        Handler(Looper.getMainLooper())

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_dual_player
        )

        rootLayout =
            findViewById(R.id.rootLayout)

        playerView1 =
            findViewById(R.id.playerView1)

        playerView2 =
            findViewById(R.id.playerView2)

        list1 =
            findViewById(R.id.listChannels1)

        list2 =
            findViewById(R.id.listChannels2)

        updateOrientation()

        playerView1.resizeMode =
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        playerView2.resizeMode =
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        list1.visibility = View.GONE
        list2.visibility = View.GONE

        player1 =
            ExoPlayer.Builder(this)
                .build()

        player2 =
            ExoPlayer.Builder(this)
                .build()

        playerView1.player = player1
        playerView2.player = player2

        playerView1.setOnClickListener {

            list2.visibility = View.GONE

            if (list1.visibility == View.VISIBLE) {

                list1.visibility = View.GONE

            } else {

                list1.visibility = View.VISIBLE

                autoHideList(list1)
            }
        }

        playerView2.setOnClickListener {

            list1.visibility = View.GONE

            if (list2.visibility == View.VISIBLE) {

                list2.visibility = View.GONE

            } else {

                list2.visibility = View.VISIBLE

                autoHideList(list2)
            }
        }

        intent.getStringExtra("url1")?.let {
            playOnPlayer1(it)
        }

        intent.getStringExtra("url2")?.let {
            playOnPlayer2(it)
        }

        loadLists()
    }

    private fun updateOrientation() {

        if (
            resources.configuration.orientation ==
            Configuration.ORIENTATION_LANDSCAPE
        ) {

            rootLayout.orientation =
                LinearLayout.HORIZONTAL

        } else {

            rootLayout.orientation =
                LinearLayout.VERTICAL
        }
    }

    override fun onConfigurationChanged(
        newConfig: Configuration
    ) {

        super.onConfigurationChanged(newConfig)

        updateOrientation()
    }

    private fun loadLists() {

        val names =
            channels.map { it.name }

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

            handler.postDelayed({

                list1.visibility =
                    View.GONE

            }, 3000)
        }

        list2.setOnItemClickListener {
                _, _, position, _ ->

            playOnPlayer2(
                channels[position].url
            )

            handler.postDelayed({

                list2.visibility =
                    View.GONE

            }, 3000)
        }
    }

    private fun autoHideList(
        list: ListView
    ) {

        handler.postDelayed({

            list.visibility =
                View.GONE

        }, 5000)
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
