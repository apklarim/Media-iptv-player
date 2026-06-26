package com.media.iptvplayer

import com.media.iptvplayer.model.Channel

object M3uParser {

    fun parse(content: String): MutableList<Channel> {

        val channels = mutableListOf<Channel>()

        val lines = content.lines()

        var currentName = ""
        var currentGroup = ""
        var currentLogo = ""

        for (line in lines) {

            if (line.startsWith("#EXTINF")) {

                currentName =
                    line.substringAfterLast(",")

                currentGroup =
                    Regex("""group-title="([^"]*)"""")
                        .find(line)
                        ?.groupValues?.get(1)
                        ?: ""

                currentLogo =
                    Regex("""tvg-logo="([^"]*)"""")
                        .find(line)
                        ?.groupValues?.get(1)
                        ?: ""
            }

            else if (
                line.startsWith("http://") ||
                line.startsWith("https://")
            ) {

                val cleanUrl =
                    line.substringBefore("|")

                val userAgent =
                    if (line.contains("|User-Agent="))
                        line.substringAfter("|User-Agent=")
                    else
                        ""

                val group =
                    currentGroup.lowercase()

                val category = when {

                    group.contains("movie") ||
                    group.contains("movies") ||
                    group.contains("film") ||
                    group.contains("films") ||
                    group.contains("vod") ||
                    group.contains("cinema") ||
                    group.contains("sinema") ||
                    group.contains("netflix") ||
                    group.contains("amazon") ||
                    group.contains("disney") ||
                    group.contains("blu tv") ||
                    group.contains("gain") ->

                        "MOVIE"

                    group.contains("series") ||
                    group.contains("serial") ||
                    group.contains("dizi") ||
                    group.contains("shows") ||
                    group.contains("show") ->

                        "SERIES"

                    else ->
                        "LIVE"
                }

                channels.add(

                    Channel(
                        name = currentName,
                        url = cleanUrl,
                        logo = currentLogo,
                        group = currentGroup,
                        category = category,
                        userAgent = userAgent
                    )
                )
            }
        }

        return channels
    }
                          }
