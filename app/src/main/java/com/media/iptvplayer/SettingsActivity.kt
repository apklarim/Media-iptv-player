package com.media.iptvplayer

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnAbout: Button

    private lateinit var switchAutoHide: Switch
    private lateinit var switchLastPlaylist: Switch
    private lateinit var switchLastChannel: Switch

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_settings
        )

        btnAbout =
            findViewById(
                R.id.btnAbout
            )

        switchAutoHide =
            findViewById(
                R.id.switchAutoHide
            )

        switchLastPlaylist =
            findViewById(
                R.id.switchLastPlaylist
            )

        switchLastChannel =
            findViewById(
                R.id.switchLastChannel
            )

        // Kayıtlı ayarları yükle

        switchAutoHide.isChecked =
            SettingsPreferences
                .isAutoHideEnabled(this)

        switchLastPlaylist.isChecked =
            SettingsPreferences
                .isAutoLoadLastPlaylistEnabled(this)

        switchLastChannel.isChecked =
            SettingsPreferences
                .isAutoLoadLastChannelEnabled(this)

        // Player butonlarını otomatik gizle

        switchAutoHide
            .setOnCheckedChangeListener {
                    _, isChecked ->

                SettingsPreferences
                    .setAutoHideEnabled(
                        this,
                        isChecked
                    )
            }

        // Son listeyi otomatik aç

        switchLastPlaylist
            .setOnCheckedChangeListener {
                    _, isChecked ->

                SettingsPreferences
                    .setAutoLoadLastPlaylistEnabled(
                        this,
                        isChecked
                    )
            }

        // Son kanalı otomatik aç

        switchLastChannel
            .setOnCheckedChangeListener {
                    _, isChecked ->

                SettingsPreferences
                    .setAutoLoadLastChannelEnabled(
                        this,
                        isChecked
                    )
            }

        btnAbout.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle(
                    "Media IPTV Player"
                )
                .setMessage(
                    """
Sürüm : 1.1

Media IPTV Player

Telefon, Tablet,
TV ve TV Box için
gelişmiş IPTV oynatıcı.

© 2026
                    """.trimIndent()
                )
                .setPositiveButton(
                    "Tamam",
                    null
                )
                .show()
        }
    }
}
