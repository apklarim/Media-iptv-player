package com.media.iptvplayer

import com.media.iptvplayer.model.Channel

object M3uParser {

    fun parse(content: String): List<Channel> {

        val channels = mutableListOf<Channel>()

        val lines = content
            .replace("\r\n", "\n")
            .replace("\r", "\n")
            .split("\n")

        var currentName = ""
        var currentGroup = ""

        for (lineRaw in lines) {

            val line = lineRaw.trim()

            if (line.startsWith("#EXTINF")) {

                currentName = line.substringAfterLast(",")

                currentGroup =
                    Regex("""group-title="([^"]*)"""")
                        .find(line)
                        ?.groupValues
                        ?.get(1)
                        ?: ""
            }

            else if (
                line.startsWith("http://") ||
                line.startsWith("https://")
            ) {

                channels.add(
                    Channel(
                        name = currentName.ifBlank {
                            "İsimsiz Kanal"
                        },

                        url = line,

                        group = currentGroup,

                        logo = ""
                    )
                )

                currentName = ""
                currentGroup = ""
            }
        }

        return channels
    }
                          }
