package com.media.iptvplayer

import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {

    fun downloadText(url: String): String {

        val connection =
            URL(url).openConnection() as HttpURLConnection

        connection.connectTimeout = 15000
        connection.readTimeout = 15000
        connection.requestMethod = "GET"
        connection.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0"
        )

        connection.connect()

        return connection.inputStream
            .bufferedReader()
            .use { it.readText() }
    }
}
