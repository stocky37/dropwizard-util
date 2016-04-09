package com.github.stocky37.util.cache.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.cache.Cache;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.setup.Environment;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = GuavaCacheFactory.class)
public interface CacheFactory<K, V> extends Discoverable {
	<K1 extends K, V1 extends V> Cache<K1, V1> build();
	<K1 extends K, V1 extends V> Cache<K1, V1> build(Environment environment);
}
