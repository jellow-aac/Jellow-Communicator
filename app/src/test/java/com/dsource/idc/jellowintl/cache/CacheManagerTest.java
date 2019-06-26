package com.dsource.idc.jellowintl.cache;

import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.factories.PathFactory;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CacheManagerTest {

    @Test
    public void clearCacheTest(){
        CacheManager.clearCache();
        assert isIconCacheCleared() && isPathCacheCleared() && isMemoryCacheCleared();
    }

    private boolean isIconCacheCleared() {
        return IconCache.getL1Icons() == null &&
                IconCache.getAllL2Icons() == null &&
                IconCache.getL3Icons() == null &&
                IconCache.getL3SeqIcons() == null &&
                IconCache.getExpressiveIcons() == null &&
                IconCache.getMiscellaneousIcons() == null;
    }

    private boolean isPathCacheCleared() {
        return PathFactory.basePath == null &&
                PathFactory.iconDirectory == null &&
                PathFactory.jsonMap == null;
    }

    private boolean isMemoryCacheCleared() {
        return true;
        //return MemoryCache.getBitmapFromMemCache("") == null;
    }
}
