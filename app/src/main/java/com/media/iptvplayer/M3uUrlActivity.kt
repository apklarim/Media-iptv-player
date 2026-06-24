package com.media.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.media.iptvplayer.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

                val name =
                    etName.text.toString().trim()

                val url =
                    etUrl.text.toString().trim()

                if (name.isEmpty() || url.isEmpty()) {

                    Toast.makeText(
                        this,
                        "Tüm alanları doldurun",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                lifecycleScope.launch {

                    try {

                        val content =
                            withContext(Dispatchers.IO) {
                                NetworkUtils.downloadText(url)
                            }

                        val channels =
                            M3uParser.parse(content)

                        ChannelRepository.channels =
                            channels

                        PlaylistManager.addPlaylist(
                            this@M3uUrlActivity,
                            Playlist(
                                name = name,
                                type = "M3U",
                                url = url
                            )
                        )

                        Toast.makeText(
                            this@M3uUrlActivity,
                            "${channels.size} kanal yüklendi",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(
                            Intent(
                                this@M3uUrlActivity,
                                MainActivity::class.java
                            )
                        )

                        finish()

                    } catch (e: Exception) {

                        Toast.makeText(
                            this@M3uUrlActivity,
                            "Hata: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }
}
