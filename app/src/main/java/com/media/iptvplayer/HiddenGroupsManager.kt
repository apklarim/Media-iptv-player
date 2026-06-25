package com.media.iptvplayer

import android.content.Context

object HiddenGroupsManager {

    private const val PREFS = "hidden_groups"
    private const val KEY_GROUPS = "groups"

    fun hideGroup(
        context: Context,
        group: String
    ) {

        val groups =
            getHiddenGroups(context)
                .toMutableSet()

        groups.add(group)

        context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        )
            .edit()
            .putStringSet(
                KEY_GROUPS,
                groups
            )
            .apply()
    }

    fun unhideGroup(
        context: Context,
        group: String
    ) {

        val groups =
            getHiddenGroups(context)
                .toMutableSet()

        groups.remove(group)

        context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        )
            .edit()
            .putStringSet(
                KEY_GROUPS,
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
                KEY_GROUPS,
                emptySet()
            ) ?: emptySet()
    }

    // Tüm gizli grupları aç

    fun clearHiddenGroups(
        context: Context
    ) {

        context.getSharedPreferences(
            PREFS,
            Context.MODE_PRIVATE
        )
            .edit()
            .remove(KEY_GROUPS)
            .apply()
    }

    // Tek grup aç

    fun restoreGroup(
        context: Context,
        group: String
    ) {

        unhideGroup(
            context,
            group
        )
    }
}
