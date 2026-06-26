package com.media.iptvplayer

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnAbout: Button

    private lateinit var switchAutoHide: Switch
    private lateinit var switchLastPlaylist: Switch
    private lateinit var switchLastChannel: Switch

    private lateinit var radioDark: RadioButton
    private lateinit var radioTurquoise: RadioButton
    private lateinit var radioBlue: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_settings
        )

        ThemeManager.applyTheme(this)

        btnAbout =
            findViewById(R.id.btnAbout)

        switchAutoHide =
            findViewById(R.id.switchAutoHide)

        switchLastPlaylist =
            findViewById(R.id.switchLastPlaylist)

        switchLastChannel =
            findViewById(R.id.switchLastChannel)

        radioDark =
            findViewById(R.id.radioDark)

        radioTurquoise =
            findViewById(R.id.radioTurquoise)

        radioBlue =
            findViewById(R.id.radioBlue)

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

        // Tema seçimini yükle

        when (
            ThemePreferences
                .getTheme(this)
        ) {

            ThemePreferences.THEME_DARK ->
                radioDark.isChecked = true

            ThemePreferences.THEME_TURQUOISE ->
                radioTurquoise.isChecked = true

            ThemePreferences.THEME_BLUE ->
                radioBlue.isChecked = true
        }

        // Tema seçimi

        radioDark.setOnClickListener {

            ThemePreferences.saveTheme(
                this,
                ThemePreferences.THEME_DARK
            )

            restartApp()
        }

        radioTurquoise.setOnClickListener {

            ThemePreferences.saveTheme(
                this,
                ThemePreferences.THEME_TURQUOISE
            )

            restartApp()
        }

        radioBlue.setOnClickListener {

            ThemePreferences.saveTheme(
                this,
                ThemePreferences.THEME_BLUE
            )

            restartApp()
        }

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

    private fun restartApp() {

        val intent =
            packageManager
                .getLaunchIntentForPackage(
                    packageName
                )

        intent?.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        )

        startActivity(intent)

        finishAffinity()
    }
}
