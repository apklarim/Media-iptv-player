package com.media.iptvplayer

import android.content.Context

object AdultPinManager {

    private const val PREFS = "adult_pin"

    fun getPin(
        context: Context
    ): String {

        return context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .getString(
                "pin",
                "1234"
            ) ?: "1234"
    }

    fun setPin(
        context: Context,
        pin: String
    ) {

        context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                "pin",
                pin
            )
            .apply()
    }

    fun isAdultChannel(
        group: String,
        name: String
    ): Boolean {

        val text =
            "$group $name".lowercase()

        return text.contains("xxx") ||
                text.contains("adult") ||
                text.contains("18+") ||
                text.contains("18 ") ||
                text.contains("erotik") ||
                text.contains("sex") ||
                text.contains("porno") ||
                text.contains("yetişkin")
    }
}
