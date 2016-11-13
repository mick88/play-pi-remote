package com.michaldabski.radiopiremote.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Michal on 24/06/2014.
 */
public class BitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache
{
	/**
	 * Percentage of memory that we allow this cache to take up
	 */
	public final static float MAX_MEMORY_CONSUMPTION = 0.30f;

	/**
	 * Create new cache with default size
	 */
	public BitmapCache()
	{
		this(getRecommendedCacheSize());
	}

	/**
	 * Create new cache with custom size
	 * @param maxSize Cache size in bytes
	 */
	public BitmapCache(int maxSize)
	{
		super(maxSize);
	}

	@Override
	public Bitmap getBitmap(String string)
	{
		Bitmap bitmap = get(string);
        if (bitmap == null)
            return null;
        if (bitmap.isRecycled())
        {
            remove(string);
            return null;
        }
        else return bitmap;
	}

	@Override
	public void putBitmap(String string, Bitmap bitmap)
	{
		put(string, bitmap);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected int sizeOf(String key, Bitmap value)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			return value.getByteCount();
		else
			return value.getAllocationByteCount();
	}

	public static int getRecommendedCacheSize()
	{
		int maxMemory = (int) Math.min(Runtime.getRuntime().maxMemory(), (long) Integer.MAX_VALUE);
		if (maxMemory <= 0) throw new OutOfMemoryError("Max memory is less than 0: "+maxMemory);
		else {
			return (int) (MAX_MEMORY_CONSUMPTION * maxMemory);
		}
	}
}
