package com.github.stocky37.util.cache.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;

public class GuavaLoadingCacheFactory<K, V> extends GuavaCacheFactory<K, V> implements LoadingCacheFactory<K, V> {

	@JsonProperty
	public GuavaCacheFactory refreshAfterWrite(Duration duration) {
		getCacheBuilder().refreshAfterWrite(duration.getQuantity(), duration.getUnit());
		return this;
	}

	@Override
	public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<K1, V1> loader) {
		return getCacheBuilder().build(loader);
	}

	@Override
	public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<K1, V1> loader, Environment environment) {
		return build(loader);
	}
}
