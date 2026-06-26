package com.media.iptvplayer

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var btnLiveTv: LinearLayout
    private lateinit var btnMovies: LinearLayout
    private lateinit var btnSeries: LinearLayout
    private lateinit var btnPlaylists: LinearLayout
    private lateinit var btnSettings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        ThemeManager.applyTheme(this)

        autoLoadLastPlaylist()

        btnLiveTv = findViewById(R.id.btnLiveTv)
        btnMovies = findViewById(R.id.btnMovies)
        btnSeries = findViewById(R.id.btnSeries)
        btnPlaylists = findViewById(R.id.btnPlaylists)
        btnSettings = findViewById(R.id.btnSettings)

        applyCardTheme(btnLiveTv)
        applyCardTheme(btnMovies)
        applyCardTheme(btnSeries)
        applyCardTheme(btnPlaylists)
        applyCardTheme(btnSettings)

        val focusViews = listOf(
            btnLiveTv,
            btnMovies,
            btnSeries,
            btnPlaylists,
            btnSettings
        )

        focusViews.forEach { view ->

            view.isFocusable = true
            view.isFocusableInTouchMode = true

            view.setOnFocusChangeListener { v, hasFocus ->

                val anim =
                    if (hasFocus)
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.focus_gain
                        )
                    else
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.focus_lost
                        )

                v.startAnimation(anim)
            }
        }

        btnLiveTv.setOnClickListener {

            animateButton(btnLiveTv)

            startActivity(
                Intent(
                    this,
                    ChannelListActivity::class.java
                ).putExtra(
                    "CATEGORY",
                    "LIVE"
                )
            )
        }

        btnMovies.setOnClickListener {

            animateButton(btnMovies)

            startActivity(
                Intent(
                    this,
                    ChannelListActivity::class.java
                ).putExtra(
                    "CATEGORY",
                    "MOVIE"
                )
            )
        }

        btnSeries.setOnClickListener {

            animateButton(btnSeries)

            startActivity(
                Intent(
                    this,
                    ChannelListActivity::class.java
                ).putExtra(
                    "CATEGORY",
                    "SERIES"
                )
            )
        }

        btnPlaylists.setOnClickListener {

            animateButton(btnPlaylists)

            startActivity(
                Intent(
                    this,
                    PlaylistListActivity::class.java
                )
            )
        }

        btnPlaylists.setOnLongClickListener {

            startActivity(
                Intent(
                    this,
                    HiddenGroupsActivity::class.java
                )
            )

            true
        }

        btnSettings.setOnClickListener {

            animateButton(btnSettings)

            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }
    }

    private fun applyCardTheme(
        layout: LinearLayout
    ) {

        val drawable =
            GradientDrawable()

        drawable.cornerRadius = 18f

        drawable.setColor(
            ThemeManager.getCardColor(this)
        )

        drawable.setStroke(
            2,
            ThemeManager.getAccentColor(this)
        )

        layout.background = drawable
    }

    private fun animateButton(view: View) {

        view.animate()
            .scaleX(1.08f)
            .scaleY(1.08f)
            .setDuration(120)
            .withEndAction {

                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(120)
                    .start()
            }
            .start()
    }

    private fun autoLoadLastPlaylist() {

        if (!SettingsPreferences
                .isAutoLoadLastPlaylistEnabled(this)) {
            return
        }

        val lastId =
            LastPlaylistManager
                .getLastPlaylistId(this)

        if (lastId == -1L)
            return

        val playlist =
            PlaylistManager
                .getPlaylists(this)
                .find {
                    it.id == lastId
                } ?: return

        lifecycleScope.launch {

            try {

                val content =

                    if (playlist.type == "M3U_FILE") {

                        withContext(Dispatchers.IO) {

                            contentResolver
                                .openInputStream(
                                    Uri.parse(
                                        playlist.url
                                    )
                                )
                                ?.bufferedReader()
                                ?.use {
                                    it.readText()
                                } ?: ""
                        }

                    } else {

                        withContext(Dispatchers.IO) {

                            NetworkUtils
                                .downloadText(
                                    playlist.url
                                )
                        }
                    }

                ChannelRepository.setChannels(
                    M3uParser.parse(content)
                )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}
