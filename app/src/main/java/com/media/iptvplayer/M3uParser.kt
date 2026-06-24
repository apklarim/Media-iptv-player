package com.media.iptvplayer

import android.util.Log
import com.media.iptvplayer.model.Channel

object M3uParser {

    fun parse(content: String): List<Channel> {

        Log.d("M3U_TEST", "Dosya boyutu = ${content.length}")

        val channels = mutableListOf<Channel>()

        val lines = content.lines()

        var currentName = ""

        for (lineRaw in lines) {

            val line = lineRaw.trim()

            if (line.startsWith("#EXTINF")) {

                currentName =
                    line.substringAfterLast(",")
                        .trim()

                Log.d(
                    "M3U_TEST",
                    "Kanal adı = $currentName"
                )
            }

            else if (
                line.startsWith("http://") ||
                line.startsWith("https://")
            ) {

                Log.d(
                    "M3U_TEST",
                    "URL bulundu = $line"
                )

                channels.add(
                    Channel(
                        name =
                        if (currentName.isEmpty())
                            "İsimsiz Kanal"
                        else currentName,

                        url = line,
                        logo = "",
                        group = ""
                    )
                )
            }
        }

        Log.d(
            "M3U_TEST",
            "Toplam kanal = ${channels.size}"
        )

        return channels
    }
}
