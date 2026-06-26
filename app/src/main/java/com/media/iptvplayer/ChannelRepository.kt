package com.media.iptvplayer

import com.media.iptvplayer.model.Channel

object ChannelRepository {

    val channels = mutableListOf<Channel>()

    fun setChannels(list: List<Channel>) {

        channels.clear()

        channels.addAll(list)
    }

    fun clear() {

        channels.clear()
    }
}
