package com.media.iptvplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        autoLoadLastPlaylist()

        btnLiveTv = findViewById(R.id.btnLiveTv)
        btnMovies = findViewById(R.id.btnMovies)
        btnSeries = findViewById(R.id.btnSeries)
        btnPlaylists = findViewById(R.id.btnPlaylists)

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

        // Uzun basınca Gizli Gruplar ekranı açılır

        btnPlaylists.setOnLongClickListener {

            startActivity(
                Intent(
                    this,
                    HiddenGroupsActivity::class.java
                )
            )

            true
        }
    }

    private fun animateButton(view: View) {

        view.animate()
            .scaleX(1.04f)
            .scaleY(1.04f)
            .setDuration(100)
            .withEndAction {

                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun autoLoadLastPlaylist() {

        val lastId =
            LastPlaylistManager
                .getLastPlaylistId(this)

        if (lastId == -1L) return

        val playlist =
            PlaylistManager
                .getPlaylists(this)
                .find {
                    it.id == lastId
                } ?: return

        lifecycleScope.launch {

            try {

                var content = ""

                if (playlist.type == "M3U_FILE") {

                    content =
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

                    content =
                        withContext(Dispatchers.IO) {

                            NetworkUtils.downloadText(
                                playlist.url
                            )
                        }
                }

                ChannelRepository.channels =
                    M3uParser.parse(content)
                        .toMutableList()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}
