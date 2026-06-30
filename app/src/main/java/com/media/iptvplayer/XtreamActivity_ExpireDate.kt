package com.media.iptvplayer

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.media.iptvplayer.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class XtreamActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var loadingLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_xtream)

        prefs = getSharedPreferences(
            "drafts",
            MODE_PRIVATE
        )

        loadingLayout =
            findViewById(R.id.loadingLayout)

        val etName =
            findViewById<EditText>(R.id.etXtreamName)

        val etServer =
            findViewById<EditText>(R.id.etServer)

        val etUser =
            findViewById<EditText>(R.id.etUsername)

        val etPass =
            findViewById<EditText>(R.id.etPassword)

        etServer.setText(
            prefs.getString("xtream_server", "")
        )

        etUser.setText(
            prefs.getString("xtream_user", "")
        )

        etPass.setText(
            prefs.getString("xtream_pass", "")
        )

        findViewById<Button>(R.id.btnSaveXtream)
            .setOnClickListener {

                val playlistName =
                    etName.text.toString().trim()

                val server =
                    etServer.text.toString().trim()

                val user =
                    etUser.text.toString().trim()

                val pass =
                    etPass.text.toString().trim()

                if (server.isEmpty() ||
                    user.isEmpty() ||
                    pass.isEmpty()
                ) {

                    Toast.makeText(
                        this,
                        "Tüm alanları doldurun",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                loadingLayout.visibility =
                    View.VISIBLE

                val m3uUrl =
                    "$server/get.php?username=$user&password=$pass&type=m3u_plus"

                lifecycleScope.launch {

                    try {

                        val content =
                            withContext(Dispatchers.IO) {
                                NetworkUtils.downloadText(m3uUrl)
                            }

                        val channels =
                            withContext(Dispatchers.Default) {
                                M3uParser.parse(content)
                            }

                        ChannelRepository.setChannels(
                            channels
                        )

                        // Şimdilik exp_date bilgisi alınamıyorsa "Bilinmiyor" kaydediyoruz.
                        // Sonraki sürümde player_api.php üzerinden gerçek tarih okunabilir.

                        PlaylistManager.addPlaylist(
                            this@XtreamActivity,
                            Playlist(
                                name =
                                    if (playlistName.isEmpty())
                                        user
                                    else
                                        playlistName,

                                type = "XTREAM",
                                url = m3uUrl,
                                server = server,
                                username = user,
                                password = pass,
                                expireDate = "Bilinmiyor"
                            )
                        )

                        prefs.edit().clear().apply()

                        loadingLayout.visibility =
                            View.GONE

                        Toast.makeText(
                            this@XtreamActivity,
                            "${channels.size} kanal bulundu",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(
                            Intent(
                                this@XtreamActivity,
                                MainActivity::class.java
                            )
                        )

                        finish()

                    } catch (e: Exception) {

                        loadingLayout.visibility =
                            View.GONE

                        Toast.makeText(
                            this@XtreamActivity,
                            "Xtream hatası: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    override fun onPause() {

        super.onPause()

        prefs.edit()
            .putString(
                "xtream_server",
                findViewById<EditText>(
                    R.id.etServer
                ).text.toString()
            )
            .putString(
                "xtream_user",
                findViewById<EditText>(
                    R.id.etUsername
                ).text.toString()
            )
            .putString(
                "xtream_pass",
                findViewById<EditText>(
                    R.id.etPassword
                ).text.toString()
            )
            .apply()
    }
}
