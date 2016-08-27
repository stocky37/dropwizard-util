package com.github.stocky37.util.cache;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheLoader;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class CacheLoaders {
	public static <K, V> CacheLoader<K, V> fromFunction(Function<K, V> function) {
		return CacheLoader.from(function::apply);
	}

	public static <K, V> Function<K, V> toFunction(CacheLoader<K,V> loader) {
		return key -> {
			try {
				return loader.load(key);
			} catch(Exception e) {
				throw Throwables.propagate(e);
			}
		};
	}
}
