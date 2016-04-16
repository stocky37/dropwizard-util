package me.stockwells.util.cache;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

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
