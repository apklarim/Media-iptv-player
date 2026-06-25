package com.media.iptvplayer

import android.content.Context

object HiddenGroupsManager {

    private const val PREFS = "hidden_groups"

    fun hideGroup(
        context: Context,
        group: String
    ) {

        val groups = getHiddenGroups(context)
            .toMutableSet()

        groups.add(group)

        context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        )
            .edit()
            .putStringSet(
                "groups",
                groups
            )
            .apply()
    }

    fun unhideGroup(
        context: Context,
        group: String
    ) {

        val groups = getHiddenGroups(context)
            .toMutableSet()

        groups.remove(group)

        context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        )
            .edit()
            .putStringSet(
                "groups",
                groups
            )
            .apply()
    }

    fun isHidden(
        context: Context,
        group: String
    ): Boolean {

        return getHiddenGroups(context)
            .contains(group)
    }

    fun getHiddenGroups(
        context: Context
    ): Set<String> {

        return context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .getStringSet(
                "groups",
                emptySet()
            ) ?: emptySet()
    }
}
