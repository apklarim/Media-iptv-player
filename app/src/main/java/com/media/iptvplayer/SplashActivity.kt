package com.media.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_splash
        )

        val imgLogo =
            findViewById<ImageView>(
                R.id.imgLogo
            )

        val animation =
            AnimationUtils.loadAnimation(
                this,
                R.anim.logo_zoom
            )

        imgLogo.startAnimation(
            animation
        )

        Handler(
            Looper.getMainLooper()
        ).postDelayed({

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()

        }, 2500)
    }
}
