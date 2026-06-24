package com.media.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.cardLive)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        AddPlaylistActivity::class.java
                    )
                )
            }

        findViewById<Button>(R.id.cardMovies)
            .setOnClickListener {

            }

        findViewById<Button>(R.id.cardSeries)
            .setOnClickListener {

            }

        findViewById<Button>(R.id.cardPlaylists)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        PlaylistListActivity::class.java
                    )
                )
            }

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
