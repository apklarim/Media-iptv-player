package com.media.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnLiveTv: Button
    private lateinit var btnMovies: Button
    private lateinit var btnSeries: Button
    private lateinit var btnSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btnLiveTv = findViewById(R.id.btnLiveTv)
        btnMovies = findViewById(R.id.btnMovies)
        btnSeries = findViewById(R.id.btnSeries)
        btnSettings = findViewById(R.id.btnSettings)

        btnLiveTv.setOnClickListener {

            animateButton(btnLiveTv)

            startActivity(
                Intent(
                    this,
                    PlaylistListActivity::class.java
                )
            )
        }

        btnMovies.setOnClickListener {

            animateButton(btnMovies)

            val intent = Intent(
                this,
                ChannelListActivity::class.java
            )

            intent.putExtra("content_type", "movie")

            startActivity(intent)
        }

        btnSeries.setOnClickListener {

            animateButton(btnSeries)

            val intent = Intent(
                this,
                ChannelListActivity::class.java
            )

            intent.putExtra("content_type", "series")

            startActivity(intent)
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

    private fun animateButton(button: Button) {

        val animation =
            AnimationUtils.loadAnimation(
                this,
                android.R.anim.fade_in
            )

        button.startAnimation(animation)

        val vibrator =
            getSystemService(VIBRATOR_SERVICE)
                    as Vibrator

        vibrator.vibrate(
            VibrationEffect.createOneShot(
                40,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }
}
