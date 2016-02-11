package me.stockwells.util.cache;

import me.stockwells.util.cache.loaders.AsyncCacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListeningExecutorService;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class LoadingCacheCacheLoader<K,V> extends AsyncCacheLoader<K,V> {
	private final LoadingCache<K,V> cache;

	public LoadingCacheCacheLoader(ListeningExecutorService service, LoadingCache<K, V> cache) {
		super(service);
		this.cache = cache;
	}

	@Override
	public V load(K key) throws Exception {
		return cache.get(key);
	}
}

