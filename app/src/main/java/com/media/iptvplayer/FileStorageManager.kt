package com.media.iptvplayer

import android.content.Context
import java.io.File

object FileStorageManager {

    private fun getFolder(
        context: Context
    ): File {

        val dir = File(
            context.getExternalFilesDir(null),
            "MediaIPTV"
        )

        if (!dir.exists()) {
            dir.mkdirs()
        }

        return dir
    }

    fun writeText(
        context: Context,
        fileName: String,
        text: String
    ) {

        File(
            getFolder(context),
            fileName
        ).writeText(text)
    }

    fun readText(
        context: Context,
        fileName: String,
        defaultValue: String = ""
    ): String {

        val file =
            File(
                getFolder(context),
                fileName
            )

        return if (file.exists()) {
            file.readText()
        } else {
            defaultValue
        }
    }
}
