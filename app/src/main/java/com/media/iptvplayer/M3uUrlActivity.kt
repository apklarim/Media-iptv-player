package com.media.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.media.iptvplayer.model.Playlist

class M3uUrlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_m3u_url)

        val etName =
            findViewById<EditText>(R.id.etPlaylistName)

        val etUrl =
            findViewById<EditText>(R.id.etPlaylistUrl)

        findViewById<Button>(R.id.btnSaveM3u)
            .setOnClickListener {

                val name = etName.text.toString().trim()

                val url = etUrl.text.toString().trim()

                if (name.isEmpty() || url.isEmpty()) {

                    Toast.makeText(
                        this,
                        "Lütfen tüm alanları doldurun",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                PlaylistManager.addPlaylist(
                    this,
                    Playlist(
                        name = name,
                        type = "M3U",
                        url = url
                    )
                )

                Toast.makeText(
                    this,
                    "Liste kaydedildi",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        this,
                        PlaylistListActivity::class.java
                    )
                )

                finish()
            }
    }
}
