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

    private val channels
        get() = ChannelRepository.channels

    private val handler =
        Handler(Looper.getMainLooper())

    private var currentUrl1 = ""
    private var currentUrl2 = ""

    private var selectedPlayer = 1

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

        currentUrl1 =
            intent.getStringExtra("url1") ?: ""

        currentUrl2 =
            intent.getStringExtra("url2") ?: ""

        if (currentUrl1.isNotEmpty())
            playOnPlayer1(currentUrl1)

        if (currentUrl2.isNotEmpty())
            playOnPlayer2(currentUrl2)

        selectPlayer(1)

        playerView1.setOnClickListener {

            selectPlayer(1)

            list2.visibility = View.GONE

            list1.visibility =
                if (list1.visibility ==
                    View.VISIBLE
                )
                    View.GONE
                else
                    View.VISIBLE

            autoHideList(list1)
        }

        playerView2.setOnClickListener {

            selectPlayer(2)

            list1.visibility = View.GONE

            list2.visibility =
                if (list2.visibility ==
                    View.VISIBLE
                )
                    View.GONE
                else
                    View.VISIBLE

            autoHideList(list2)
        }

        loadLists()
    }

    private fun selectPlayer(
        playerNo: Int
    ) {

        selectedPlayer = playerNo

        if (playerNo == 1) {

            player1.volume = 1f
            player2.volume = 0f

        } else {

            player1.volume = 0f
            player2.volume = 1f
        }
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

        handler.postDelayed({

            if (currentUrl1.isNotEmpty())
                playOnPlayer1(currentUrl1)

            if (currentUrl2.isNotEmpty())
                playOnPlayer2(currentUrl2)

            selectPlayer(selectedPlayer)

        }, 500)
    }

    private fun loadLists() {

        val names =
            channels.map { it.name }

        list1.adapter =
            ArrayAdapter(
                this,
                R.layout.item_dual_channel,
                names
            )

        list2.adapter =
            ArrayAdapter(
                this,
                R.layout.item_dual_channel,
                names
            )

        list1.setOnItemClickListener {
                _, _, position, _ ->

            currentUrl1 =
                channels[position].url

            playOnPlayer1(currentUrl1)

            selectPlayer(1)

            handler.postDelayed({

                list1.visibility =
                    View.GONE

            }, 2000)
        }

        list2.setOnItemClickListener {
                _, _, position, _ ->

            currentUrl2 =
                channels[position].url

            playOnPlayer2(currentUrl2)

            selectPlayer(2)

            handler.postDelayed({

                list2.visibility =
                    View.GONE

            }, 2000)
        }
    }

    private fun autoHideList(
        list: ListView
    ) {

        handler.removeCallbacksAndMessages(null)

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
