package com.media.iptvplayer

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.media.iptvplayer.model.Channel

class PlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var channelList: ListView

    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button

    private val hideHandler =
        Handler(Looper.getMainLooper())

    private val channels: List<Channel>
        get() = ChannelRepository.channels

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        ThemeManager.applyTheme(this)

        playerView = findViewById(R.id.playerView)
        channelList = findViewById(R.id.listChannels)

        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)

        var url = intent.getStringExtra("url")

        if (url.isNullOrEmpty()) {

            if (SettingsPreferences
                    .isAutoLoadLastChannelEnabled(this)) {

                val lastChannelUrl =
                    PlayerPreferences.getLastChannel(this)

                if (!lastChannelUrl.isNullOrEmpty()) {
                    url = lastChannelUrl
                }
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

        if (channels.isNotEmpty() &&
            currentIndex >= 0 &&
            currentIndex < channels.size) {

            val category =
                channels[currentIndex]
                    .category
                    .uppercase()

            playerView.useController =
                category.contains("MOVIE") ||
                category.contains("SERIES") ||
                category.contains("FILM") ||
                category.contains("DIZI") ||
                category.contains("VOD")
        }

        playChannel(currentIndex)

        btnPrev.setOnClickListener {

            previousChannel()
            showControlsTemporarily()
        }

        btnNext.setOnClickListener {

            nextChannel()
            showControlsTemporarily()
        }

        playerView.setOnClickListener {

            channelList.visibility =
                if (channelList.visibility == View.VISIBLE)
                    View.GONE
                else
                    View.VISIBLE

            if (channelList.visibility == View.VISIBLE) {

                channelList.requestFocus()
                channelList.setSelection(currentIndex)
            }

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

        val category =
            channel.category.uppercase()

        playerView.useController =
            category.contains("MOVIE") ||
            category.contains("SERIES") ||
            category.contains("FILM") ||
            category.contains("DIZI") ||
            category.contains("VOD")

        PlayerPreferences.saveLastChannel(
            this,
            channel.url
        )

        val ua =
            if (channel.userAgent.isNotEmpty())
                channel.userAgent
            else
                "Mozilla/5.0"

        if (::player.isInitialized) {
            player.release()
        }

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
                Uri.parse(channel.url.trim())
            )

        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    private fun previousChannel() {

        if (currentIndex > 0) {

            currentIndex--

            playChannel(currentIndex)

            channelList.setSelection(currentIndex)
        }
    }

    private fun nextChannel() {

        if (currentIndex < channels.size - 1) {

            currentIndex++

            playChannel(currentIndex)

            channelList.setSelection(currentIndex)
        }
    }

    private fun loadChannelList() {

        val adapter = ArrayAdapter(
            this,
            R.layout.player_channel_item,
            R.id.txtPlayerChannel,
            channels.map { it.name }
        )

        channelList.adapter = adapter

        channelList.choiceMode =
            ListView.CHOICE_MODE_SINGLE

        channelList.setSelection(currentIndex)

        channelList.setOnItemClickListener {
                _, _, position, _ ->

            currentIndex = position

            playChannel(position)

            channelList.visibility = View.GONE

            showControlsTemporarily()
        }
    }

    private fun showControlsTemporarily() {

        btnPrev.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE

        if (!SettingsPreferences
                .isAutoHideEnabled(this))
            return

        hideHandler.removeCallbacksAndMessages(null)

        hideHandler.postDelayed({

            btnPrev.visibility = View.GONE
            btnNext.visibility = View.GONE

        }, 4000)
    }

    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean {

        showControlsTemporarily()

        when (keyCode) {

            KeyEvent.KEYCODE_DPAD_UP -> {

                if (channelList.visibility == View.VISIBLE) {

                    val current =
                        channelList.selectedItemPosition

                    if (current > 0) {

                        channelList.setSelection(
                            current - 1
                        )
                    }

                    return true
                }

                previousChannel()
                return true
            }

            KeyEvent.KEYCODE_DPAD_DOWN -> {

                if (channelList.visibility == View.VISIBLE) {

                    val current =
                        channelList.selectedItemPosition

                    if (current < channelList.count - 1) {

                        channelList.setSelection(
                            current + 1
                        )
                    }

                    return true
                }

                nextChannel()
                return true
            }

            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_ENTER -> {

                if (channelList.visibility == View.VISIBLE) {

                    val position =
                        channelList.selectedItemPosition

                    if (position >= 0 &&
                        position < channels.size
                    ) {

                        currentIndex = position

                        playChannel(position)

                        channelList.visibility = View.GONE

                        showControlsTemporarily()
                    }

                    return true
                }

                channelList.visibility = View.VISIBLE

                channelList.requestFocus()
                channelList.setSelection(currentIndex)

                return true
            }

            KeyEvent.KEYCODE_BACK -> {

                if (channelList.visibility == View.VISIBLE) {

                    channelList.visibility = View.GONE

                    return true
                }
            }
        }

        return super.onKeyDown(
            keyCode,
            event
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
