package com.github.stocky37.util.cache.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.setup.Environment;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = GuavaLoadingCacheFactory.class)
public interface LoadingCacheFactory<K, V> extends Discoverable {
	<K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<K1, V1> loader);
}
