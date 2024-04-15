package com.example.imagetask;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryCache {
 //   private Map<String, SoftReference<Bitmap>> cache= Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());
    private Map<String, SoftReference<Bitmap>> cache;

    public MemoryCache() {
        cache = new HashMap<>();
    }

    public void put(String id, Bitmap bitmap){
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }
    public Bitmap get(String url){
       /* if(!cache.containsKey(id))
            return null;
            //return null;
        SoftReference<Bitmap> ref=cache.get(id);
        return ref.get();*/
        Bitmap bitmap = null;
        SoftReference<Bitmap> ref = cache.get(url);
        if (ref != null) {
            bitmap = ref.get(); // Returns the Bitmap if the SoftReference is not null
        }
        return bitmap;
    }


    public void clear() {
        cache.clear();
    }
}
