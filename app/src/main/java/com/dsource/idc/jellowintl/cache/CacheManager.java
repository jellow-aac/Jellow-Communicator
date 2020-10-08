package com.dsource.idc.jellowintl.cache;

import static com.dsource.idc.jellowintl.cache.IconCache.clearIconCache;
import static com.dsource.idc.jellowintl.cache.MemoryCache.clearMemoryCache;
import static com.dsource.idc.jellowintl.factories.PathFactory.clearPathCache;

public class CacheManager {
    public static void clearCache(){
        clearIconCache();
        clearPathCache();
        clearMemoryCache();
    }
}
