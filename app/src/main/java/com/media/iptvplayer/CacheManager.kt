package com.media.iptvplayer

import android.content.Context
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

object CacheManager {

    private var simpleCache: SimpleCache? = null

    fun getCache(
        context: Context
    ): SimpleCache {

        if (simpleCache == null) {

            val cacheSize =
                300L * 1024L * 1024L // 300 MB

            simpleCache = SimpleCache(
                File(
                    context.cacheDir,
                    "media_cache"
                ),
                LeastRecentlyUsedCacheEvictor(
                    cacheSize
                ),
                StandaloneDatabaseProvider(
                    context
                )
            )
        }

        return simpleCache!!
    }
}
