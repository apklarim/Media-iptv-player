package com.media.iptvplayer

import com.media.iptvplayer.model.Channel

object M3uParser {

    fun parse(content: String): List<Channel> {

        val channels = mutableListOf<Channel>()

        val normalizedContent =
            content.replace("\r\n", "\n")
                .replace("\r", "\n")

        val lines = normalizedContent.lines()

        var currentName = ""
        var currentGroup = ""
        var currentLogo = ""

        for (i in lines.indices) {

            val line = lines[i].trim()

            if (line.startsWith("#EXTINF")) {

                currentName =
                    line.substringAfterLast(",").trim()

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

                if (i + 1 < lines.size) {

                    val nextLine =
                        lines[i + 1].trim()

                    if (nextLine.startsWith("http")) {

                        channels.add(
                            Channel(
                                name = if (currentName.isBlank())
                                    "İsimsiz Kanal"
                                else currentName,

                                url = nextLine,
                                logo = currentLogo,
                                group = currentGroup
                            )
                        )
                    }
                }
            }
        }

        return channels
    }
                          }
