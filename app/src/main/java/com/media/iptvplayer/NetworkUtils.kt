package com.media.iptvplayer

import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {

    fun downloadText(url: String): String {

        val connection =
            URL(url).openConnection() as HttpURLConnection

        // Büyük IPTV listeleri için süreyi artırıyoruz

        connection.connectTimeout = 60000
        connection.readTimeout = 60000

        connection.requestMethod = "GET"

        connection.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0"
        )

        connection.setRequestProperty(
            "Accept",
            "*/*"
        )

        connection.setRequestProperty(
            "Connection",
            "keep-alive"
        )

        connection.doInput = true

        connection.connect()

        val responseCode = connection.responseCode

        if (responseCode != HttpURLConnection.HTTP_OK) {

            throw Exception(
                "Sunucu hatası: $responseCode"
            )
        }

        return connection.inputStream
            .bufferedReader()
            .use { it.readText() }
    }
}
