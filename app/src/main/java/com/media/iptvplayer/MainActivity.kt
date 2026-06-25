package com.media.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnLiveTv: LinearLayout
    private lateinit var btnMovies: LinearLayout
    private lateinit var btnSeries: LinearLayout
    private lateinit var btnPlaylists: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btnLiveTv =
            findViewById(R.id.btnLiveTv)

        btnMovies =
            findViewById(R.id.btnMovies)

        btnSeries =
            findViewById(R.id.btnSeries)

        btnPlaylists =
            findViewById(R.id.btnPlaylists)

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
    }

    private fun animateButton(
        view: View
    ) {

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
}
