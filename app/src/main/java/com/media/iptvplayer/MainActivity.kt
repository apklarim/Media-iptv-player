package com.media.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // CANLI TV

        findViewById<Button>(R.id.cardLive)
            .setOnClickListener {

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

        // FILMLER

        findViewById<Button>(R.id.cardMovies)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        ChannelListActivity::class.java
                    ).putExtra(
                        "CATEGORY",
                        "MOVIES"
                    )
                )
            }

        // DIZILER

        findViewById<Button>(R.id.cardSeries)
            .setOnClickListener {

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

        // KAYITLI LISTELER

        findViewById<Button>(R.id.cardPlaylists)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        PlaylistListActivity::class.java
                    )
                )
            }

        // AYARLAR

        findViewById<Button>(R.id.cardSettings)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        SettingsActivity::class.java
                    )
                )
            }
    }
}
