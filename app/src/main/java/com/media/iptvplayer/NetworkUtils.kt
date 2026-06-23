package com.media.iptvplayer

import java.net.URL

object NetworkUtils {

    fun downloadText(url: String): String {

        return URL(url)
            .openConnection()
            .getInputStream()
            .bufferedReader()
            .use { it.readText() }
    }
}
