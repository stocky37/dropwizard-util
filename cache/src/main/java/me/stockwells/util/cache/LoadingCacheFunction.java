package me.stockwells.util.cache;

import com.google.common.base.Function;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.google.common.base.Preconditions.checkNotNull;

@ParametersAreNonnullByDefault
public class LoadingCacheFunction<K, V> implements Function<K, V> {
	private final LoadingCache<K, V> cache;

	public LoadingCacheFunction(LoadingCache<K, V> cache) {
		this.cache = cache;
	}

	@Nullable
	@Override
	public V apply(@Nullable K input) {
		return cache.getUnchecked(checkNotNull(input));
	}
}

