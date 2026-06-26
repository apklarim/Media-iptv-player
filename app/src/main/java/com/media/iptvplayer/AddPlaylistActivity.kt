package com.media.iptvplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.media.iptvplayer.model.Playlist

class AddPlaylistActivity : AppCompatActivity() {

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

                val intent =
                    Intent(Intent.ACTION_OPEN_DOCUMENT)

                intent.addCategory(
                    Intent.CATEGORY_OPENABLE
                )

                intent.type = "*/*"

                intent.putExtra(
                    Intent.EXTRA_ALLOW_MULTIPLE,
                    true
                )

                startActivityForResult(
                    intent,
                    1001
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            requestCode == 1001 &&
            resultCode == RESULT_OK
        ) {

            data?.clipData?.let { clip ->

                for (i in 0 until clip.itemCount) {

                    processUri(
                        clip.getItemAt(i).uri
                    )
                }

                finish()
                return
            }

            data?.data?.let {

                processUri(it)
                finish()
            }
        }
    }

    private fun processUri(uri: Uri) {

        try {

            contentResolver
                .takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

        } catch (_: Exception) {
        }

        try {

            val content =
                contentResolver
                    .openInputStream(uri)
                    ?.bufferedReader()
                    ?.use { it.readText() }
                    ?: ""

            val channels =
                M3uParser.parse(content)

            ChannelRepository.setChannels(channels)

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
                "$fileName eklendi",
                Toast.LENGTH_SHORT
            ).show()

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Dosya okunamadı: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
