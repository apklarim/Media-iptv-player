package com.media.iptvplayer

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.media.iptvplayer.model.Playlist

class AddPlaylistActivity : AppCompatActivity() {

    private var playlistName = "M3U Dosyası"

    private val filePicker =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                val uri: Uri? = result.data?.data

                if (uri != null) {

                    try {

                        val flags =
                            Intent.FLAG_GRANT_READ_URI_PERMISSION

                        try {
                            contentResolver.takePersistableUriPermission(
                                uri,
                                flags
                            )
                        } catch (_: Exception) {
                        }

                        val inputStream =
                            contentResolver.openInputStream(uri)

                        val content =
                            inputStream?.bufferedReader()
                                ?.use { it.readText() } ?: ""

                        val channels =
                            M3uParser.parse(content)

                        ChannelRepository.channels = channels

                        PlaylistManager.addPlaylist(
                            this,
                            Playlist(
                                name = playlistName,
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

                val editText = EditText(this)
                editText.hint = "Liste adı"

                AlertDialog.Builder(this)
                    .setTitle("Liste Adı")
                    .setView(editText)

                    .setPositiveButton("Devam") { _, _ ->

                        playlistName =
                            editText.text.toString()
                                .ifBlank {
                                    "M3U Dosyası"
                                }

                        val intent =
                            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "*/*"
                            }

                        filePicker.launch(intent)
                    }

                    .setNegativeButton("İptal", null)
                    .show()
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
