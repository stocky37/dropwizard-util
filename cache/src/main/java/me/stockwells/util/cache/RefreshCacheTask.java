package me.stockwells.util.cache;

import com.google.common.cache.LoadingCache;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;


@ParametersAreNonnullByDefault
public class RefreshCacheTask<V> extends CacheTask<String,V, LoadingCache<String,V>> {

	public RefreshCacheTask(LoadingCache<String, V> cache) {
		this(Optional.<String>empty(), cache);
	}

	public RefreshCacheTask(Optional<String> name, LoadingCache<String, V> cache) {
		super(name.orElse("refresh-cache"), cache);
	}

	@Override
	protected void execute(LoadingCache<String, V> cache) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void execute(LoadingCache<String, V> cache, Iterable<String> keys) throws Exception {
		keys.forEach(cache::refresh);
	}
}
