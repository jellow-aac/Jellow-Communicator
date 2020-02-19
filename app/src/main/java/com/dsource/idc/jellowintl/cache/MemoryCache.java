package com.dsource.idc.jellowintl.cache;

import android.graphics.Bitmap;

import androidx.collection.LruCache;

public class MemoryCache {

    private static LruCache<String, Bitmap> bitmapLruCache;

    public static void init(int cacheSize){
        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            if(bitmapLruCache != null)
            bitmapLruCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        if(bitmapLruCache != null)
        return bitmapLruCache.get(key);
        else return null;
    }

    public static void clearMemoryCache(){
        bitmapLruCache = null;
    }

}
