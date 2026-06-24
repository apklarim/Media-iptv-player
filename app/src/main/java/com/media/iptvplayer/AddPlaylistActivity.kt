package com.media.iptvplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.media.iptvplayer.model.Playlist

class AddPlaylistActivity : AppCompatActivity() {

    private val filePicker =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->

            if (uri != null) {

                try {

                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )

                } catch (_: Exception) {
                }

                try {

                    val inputStream =
                        contentResolver.openInputStream(uri)

                    val content =
                        inputStream?.bufferedReader()
                            ?.use { it.readText() } ?: ""

                    val channels =
                        M3uParser.parse(content)

                    ChannelRepository.channels = channels

                    val fileName =
                        uri.lastPathSegment
                            ?.substringAfterLast("/")
                            ?: "M3U Dosyası"

                    PlaylistManager.addPlaylist(
                        this,
                        Playlist(
                            name = fileName,
                            type = "M3U_FILE",
                            url = uri.toString()
                        )
                    )

                    Toast.makeText(
                        this,
                        "Toplam ${channels.size} kanal bulundu",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )

                    finish()

                } catch (e: Exception) {

                    Toast.makeText(
                        this,
                        "Dosya okunamadı: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_playlist)

        findViewById<Button>(R.id.btnM3uUrl)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        M3uUrlActivity::class.java
                    )
                )
            }

        findViewById<Button>(R.id.btnM3uFile)
            .setOnClickListener {

                filePicker.launch(
                    arrayOf(
                        "*/*"
                    )
                )
            }

        findViewById<Button>(R.id.btnXtream)
            .setOnClickListener {

                startActivity(
                    Intent(
                        this,
                        XtreamActivity::class.java
                    )
                )
            }
    }
}
