package me.stockwells.util.cache.loaders;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;


public abstract class AsyncCacheLoader<K,V> extends CacheLoader<K,V> {

	private final ListeningExecutorService service;

	public AsyncCacheLoader(ListeningExecutorService service) {
		this.service = service;
	}

	@Override
	public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
		return service.submit(() -> load(key));
	}
}
