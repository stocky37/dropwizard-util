package me.stockwells.util.cache;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

@ParametersAreNonnullByDefault
public class CacheLoaders {

	public static <K, V> CacheLoader<K, V> from(LoadingCache<K, V> loadingCache) {
		return from(k -> {
			return loadingCache.getUnchecked(checkNotNull(k));
		});
	}

	public static <K, V> CacheLoader<K, V> from(Function<K, V> function) {
		return new FunctionToCacheLoader<>(function);
	}

	private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> {
		private final Function<K, V> computingFunction;

		FunctionToCacheLoader(Function<K, V> computingFunction) {
			this.computingFunction = checkNotNull(computingFunction);
		}

		@Override
		public V load(K key) {
			return computingFunction.apply(checkNotNull(key));
		}
	}


}
