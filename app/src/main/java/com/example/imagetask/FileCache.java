package com.example.imagetask;

import android.content.Context;
import java.io.File;

public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        // Find the cache directory (usually /data/data/your.package.name/cache)
        cacheDir = context.getCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdirs(); // Create the cache directory if it doesn't exist
        }
    }

    public File getFile(String url) {
        // Use the URL as the filename (you may need to sanitize it)
        String filename = String.valueOf(url.hashCode());
        // Return the file object inside the cache directory
        return new File(cacheDir, filename);
    }

    public void clear() {
        // Delete all files inside the cache directory
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}
