package com.media.iptvplayer

import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {

    fun downloadText(urlString: String): String {

        val url = URL(urlString)

        val connection =
            url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"

        connection.connectTimeout = 15000
        connection.readTimeout = 15000

        connection.setRequestProperty(
            "User-Agent",
            "VLC/3.0.18 LibVLC/3.0.18"
        )

        connection.instanceFollowRedirects = true

        connection.connect()

        val code = connection.responseCode

        if (code != HttpURLConnection.HTTP_OK) {

            throw Exception(
                "Sunucu cevap kodu: $code"
            )
        }

        return connection.inputStream
            .bufferedReader()
            .readText()
    }
}
