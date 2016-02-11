package me.stockwells.util.cache;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class LoadingCacheCacheLoader<K,V> extends CacheLoader<K,V> {

	private final LoadingCache<K,V> cache;

	public LoadingCacheCacheLoader(LoadingCache<K, V> cache) {
		this.cache = cache;
	}

	@Override
	public V load(K key) throws Exception {
		return cache.get(key);
	}
}

