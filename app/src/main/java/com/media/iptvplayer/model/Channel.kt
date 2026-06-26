package com.media.iptvplayer.model

data class Channel(

    val name: String,

    val url: String,

    val logo: String = "",

    val group: String = "",

    val category: String = "LIVE",

    val userAgent: String = "",

    val referer: String = "",

    var isFavorite: Boolean = false
)
