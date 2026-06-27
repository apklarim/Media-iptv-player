package com.media.iptvplayer

import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPInputStream

object NetworkUtils {

    fun downloadText(urlString: String): String {

        val connection =
            (URL(urlString)
                .openConnection() as HttpURLConnection)

        connection.requestMethod = "GET"

        connection.connectTimeout = 10000
        connection.readTimeout = 30000

        connection.instanceFollowRedirects = true

        connection.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0"
        )

        connection.setRequestProperty(
            "Accept-Encoding",
            "gzip"
        )

        connection.setRequestProperty(
            "Connection",
            "Keep-Alive"
        )

        connection.connect()

        if (connection.responseCode
            != HttpURLConnection.HTTP_OK
        ) {

            throw Exception(
                "Sunucu cevap kodu: ${connection.responseCode}"
            )
        }

        val inputStream =

            if (
                "gzip".equals(
                    connection.contentEncoding,
                    ignoreCase = true
                )
            ) {

                GZIPInputStream(
                    BufferedInputStream(
                        connection.inputStream,
                        64 * 1024
                    )
                )

            } else {

                BufferedInputStream(
                    connection.inputStream,
                    64 * 1024
                )
            }

        return inputStream
            .bufferedReader()
            .use {
                it.readText()
            }
    }
}
