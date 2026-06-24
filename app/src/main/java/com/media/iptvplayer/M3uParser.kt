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

                val group =
                    currentGroup.lowercase()

                val category = when {

                    // FILMLER

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

                        "MOVIES"

                    // DIZILER

                    group.contains("series") ||
                    group.contains("serial") ||
                    group.contains("dizi") ||
                    group.contains("shows") ||
                    group.contains("show") ->

                        "SERIES"

                    // CANLI TV

                    else ->
                        "LIVE"
                }

                channels.add(

                    Channel(
                        name = currentName,
                        url = line,
                        logo = currentLogo,
                        group = currentGroup,
                        category = category
                    )
                )
            }
        }

        return channels
    }
                          }
