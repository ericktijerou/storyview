package com.ericktijerou.storyview

import android.app.Application
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

class StoryApp : Application() {

    companion object {
        var simpleCache: SimpleCache? = null
        var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor? = null
        lateinit var exoDatabaseProvider: ExoDatabaseProvider
        var exoPlayerCacheSize: Long = 90 * 1024 * 1024
    }

    override fun onCreate() {
        super.onCreate()
        if (leastRecentlyUsedCacheEvictor.isNull()) {
            leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize)
        }
        exoDatabaseProvider = ExoDatabaseProvider(this)
        if (simpleCache.isNull()) {
            simpleCache =
                leastRecentlyUsedCacheEvictor?.let {
                    SimpleCache(cacheDir, it, exoDatabaseProvider)
                }
        }
    }
}