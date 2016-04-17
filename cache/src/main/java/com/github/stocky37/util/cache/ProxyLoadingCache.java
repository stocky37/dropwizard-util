package com.github.stocky37.util.cache;

import com.google.common.cache.ForwardingLoadingCache;
import com.google.common.cache.LoadingCache;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;


@ParametersAreNonnullByDefault
public class ProxyLoadingCache<K, V> extends ForwardingLoadingCache<K, V> {

	private final LoadingCache<K,V> primaryCache;
	private final LoadingCache<K,V> secondaryCache;


	public ProxyLoadingCache(LoadingCache<K, V> primaryCache, LoadingCache<K, V> secondaryCache) {
		this.primaryCache = primaryCache;
		this.secondaryCache = secondaryCache;
	}

	@Override
	protected LoadingCache<K, V> delegate() {
		return primaryCache;
	}

	@Override
	public void put(K key, V value) {
		secondaryCache.put(key, value);
		super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		secondaryCache.putAll(map);
		super.putAll(map);
	}

	@Override
	public void invalidate(Object key) {
		secondaryCache.invalidate(key);
		super.invalidate(key);
	}

	@Override
	public void invalidateAll(Iterable<?> keys) {
		secondaryCache.invalidateAll(keys);
		super.invalidateAll(keys);
	}

	@Override
	public void invalidateAll() {
		secondaryCache.invalidateAll();
		super.invalidateAll();
	}

	@Override
	public void refresh(K key) {
		secondaryCache.invalidate(key);
		super.refresh(key);
	}
}
