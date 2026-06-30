package com.media.iptvplayer

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.RadioButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnAbout: Button
    private lateinit var btnAdultPin: Button
    private lateinit var btnHiddenGroups: Button

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

        btnAdultPin =
            findViewById(R.id.btnAdultPin)

        btnHiddenGroups =
            findViewById(R.id.btnHiddenGroups)

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

        // Yetişkin PIN değiştir

        btnAdultPin.setOnClickListener {

            val input = android.widget.EditText(this)

            input.hint = "Yeni PIN (4 rakam)"

            input.inputType =
                InputType.TYPE_CLASS_NUMBER

            AlertDialog.Builder(this)
                .setTitle(
                    "Yetişkin PIN Değiştir"
                )

                .setView(input)

                .setPositiveButton(
                    "Kaydet"
                ) { _, _ ->

                    val newPin =
                        input.text
                            .toString()
                            .trim()

                    if (newPin.length < 4) {

                        Toast.makeText(
                            this,
                            "PIN en az 4 rakam olmalıdır",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        AdultPinManager.setPin(
                            this,
                            newPin
                        )

                        Toast.makeText(
                            this,
                            "PIN kaydedildi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                .setNegativeButton(
                    "İptal",
                    null
                )

                .show()
        }

        btnHiddenGroups.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HiddenGroupsActivity::class.java
                )
            )
        }

        // Hakkında

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


        btnRestoreBackup.setOnClickListener {

            if (BackupManager.restoreBackup(this)) {

                Toast.makeText(
                    this,
                    "Yedek geri yüklendi. Uygulama yeniden başlatılıyor.",
                    Toast.LENGTH_LONG
                ).show()

                restartApp()

            } else {

                Toast.makeText(
                    this,
                    "Yedek bulunamadı veya geri yüklenemedi",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
