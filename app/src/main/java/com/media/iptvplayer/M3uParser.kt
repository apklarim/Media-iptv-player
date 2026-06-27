package com.media.iptvplayer

import com.media.iptvplayer.model.Channel

object M3uParser {

    private val groupRegex =
        Regex("""group-title="([^"]*)"""")

    private val logoRegex =
        Regex("""tvg-logo="([^"]*)"""")

    fun parse(content: String): MutableList<Channel> {

        val channels = ArrayList<Channel>(10000)

        var currentName = ""
        var currentGroup = ""
        var currentLogo = ""

        content.lineSequence().forEach { line ->

            when {

                line.startsWith("#EXTINF") -> {

                    currentName =
                        line.substringAfterLast(",")

                    currentGroup =
                        groupRegex.find(line)
                            ?.groupValues?.getOrNull(1)
                            ?: ""

                    currentLogo =
                        logoRegex.find(line)
                            ?.groupValues?.getOrNull(1)
                            ?: ""
                }

                line.startsWith("http://") ||
                line.startsWith("https://") -> {

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
        }

        return channels
    }
              }
