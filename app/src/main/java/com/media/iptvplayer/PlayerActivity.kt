package com.media.iptvplayer

import android.app.PictureInPictureParams
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Rational
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

class PlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var channelList: ListView

    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var btnDual: Button

    private val hideHandler =
        Handler(Looper.getMainLooper())

    private var channels =
        ChannelRepository.channels

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        ThemeManager.applyTheme(this)

        playerView = findViewById(R.id.playerView)
        channelList = findViewById(R.id.listChannels)

        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)
        btnDual = findViewById(R.id.btnDual)

        var url = intent.getStringExtra("url")

        if (SettingsPreferences
                .isAutoLoadLastChannelEnabled(this)
        ) {

            val lastChannelUrl =
                PlayerPreferences.getLastChannel(this)

            if (!lastChannelUrl.isNullOrEmpty()) {
                url = lastChannelUrl
            }
        }

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

        player.addListener(
            object : Player.Listener {

                override fun onPlayerError(
                    error: PlaybackException
                ) {

                    error.printStackTrace()
                }
            }
        )

        playerView.resizeMode =
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        playChannel(currentIndex)

        btnPrev.setOnClickListener {

            previousChannel()
            showControlsTemporarily()
        }

        btnNext.setOnClickListener {

            nextChannel()
            showControlsTemporarily()
        }

        btnDual.setOnClickListener {

            if (channels.size < 2)
                return@setOnClickListener

            val secondIndex =
                if (currentIndex + 1 < channels.size)
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

        playerView.setOnClickListener {

            showControlsTemporarily()
        }

        loadChannelList()

        showControlsTemporarily()
    }

    private fun playChannel(index: Int) {

        if (index < 0 || index >= channels.size)
            return

        currentIndex = index

        val channel = channels[index]

        PlayerPreferences.saveLastChannel(
            this,
            channel.url
        )

        // Kanalın kendi User-Agent bilgisini kullan

        val ua =
            if (channel.userAgent.isNotEmpty())
                channel.userAgent
            else
                "Mozilla/5.0"

        player.release()

        val httpDataSourceFactory =
            DefaultHttpDataSource.Factory()
                .setUserAgent(ua)
                .setAllowCrossProtocolRedirects(true)
                .setDefaultRequestProperties(
                    mapOf(
                        "User-Agent" to ua,
                        "Accept" to "*/*",
                        "Connection" to "keep-alive"
                    )
                )

        player =
            ExoPlayer.Builder(this)
                .setMediaSourceFactory(
                    DefaultMediaSourceFactory(
                        httpDataSourceFactory
                    )
                )
                .build()

        playerView.player = player

        player.addListener(
            object : Player.Listener {

                override fun onPlayerError(
                    error: PlaybackException
                ) {

                    error.printStackTrace()
                }
            }
        )

        val mediaItem =
            MediaItem.fromUri(
                Uri.parse(
                    channel.url.trim()
                )
            )

        player.setMediaItem(mediaItem)

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

        if (currentIndex < channels.size - 1) {

            currentIndex++

            playChannel(currentIndex)
        }
    }

    private fun loadChannelList() {

        channelList.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                channels.map { it.name }
            )

        channelList.setOnItemClickListener {
                _, _, position, _ ->

            playChannel(position)

            channelList.visibility = View.GONE

            showControlsTemporarily()
        }
    }

    private fun showControlsTemporarily() {

        btnPrev.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE
        btnDual.visibility = View.VISIBLE

        if (!SettingsPreferences
                .isAutoHideEnabled(this)
        ) return

        hideHandler.removeCallbacksAndMessages(null)

        hideHandler.postDelayed({

            btnPrev.visibility = View.GONE
            btnNext.visibility = View.GONE
            btnDual.visibility = View.GONE

        }, 4000)
    }

    override fun onUserLeaveHint() {

        super.onUserLeaveHint()

        if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val params =
                PictureInPictureParams
                    .Builder()
                    .setAspectRatio(
                        Rational(16, 9)
                    )
                    .build()

            enterPictureInPictureMode(params)
        }
    }

    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean {

        showControlsTemporarily()

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
                    if (channelList.visibility ==
                        View.VISIBLE
                    )
                        View.GONE
                    else
                        View.VISIBLE

                return true
            }
        }

        return super.onKeyDown(
            keyCode, event
        )
    }

    override fun onDestroy() {

        hideHandler.removeCallbacksAndMessages(null)

        if (::player.isInitialized) {
            player.release()
        }

        super.onDestroy()
    }
}
