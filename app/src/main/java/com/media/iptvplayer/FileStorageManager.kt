package com.media.iptvplayer

import java.io.File
import android.os.Environment

object FileStorageManager {

    private fun getFolder(): File {

        val dir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ),
            "MediaIPTV"
        )

        if (!dir.exists()) {
            dir.mkdirs()
        }

        return dir
    }

    fun writeText(
        fileName: String,
        text: String
    ) {

        File(
            getFolder(),
            fileName
        ).writeText(text)
    }

    fun readText(
        fileName: String,
        defaultValue: String = ""
    ): String {

        val file = File(
            getFolder(),
            fileName
        )

        return if (file.exists()) {
            file.readText()
        } else {
            defaultValue
        }
    }
}
