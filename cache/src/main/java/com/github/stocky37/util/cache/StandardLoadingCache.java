package com.github.stocky37.util.cache;

import com.google.common.base.Stopwatch;
import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.UncheckedExecutionException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@ParametersAreNonnullByDefault
public abstract class StandardLoadingCache<K, V> extends AbstractLoadingCache<K, V> {
	private static final Logger logger = Logger.getLogger(StandardLoadingCache.class.getName());

	private final CacheLoader<K, V> loader;
	private final StatsCounter stats;

	public StandardLoadingCache(CacheLoader<K, V> loader) {
		this(loader, new SimpleStatsCounter());
	}

	public StandardLoadingCache(CacheLoader<K, V> loader, StatsCounter stats) {
		this.loader = loader;
		this.stats = stats;
	}

	@Override
	public V get(@Nonnull K key) throws ExecutionException {
		return get(key, loader);
	}

	@Override
	public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
		return get(key, new CacheLoader<K, V>() {
			@Override
			public V load(K key) throws Exception {
				return valueLoader.call();
			}
		});
	}

	@Override
	public void refresh(K key) {
		loadAsync(key, loader);
	}

	private V get(K key, CacheLoader<K, V> loader) throws ExecutionException {
		final V value = getIfPresent(key);
		if(value == null) {
			stats.recordMisses(1);
			return loadSync(key, loader);
		} else {
			stats.recordHits(1);
			return value;
		}
	}

	private V loadSync(K key, CacheLoader<K, V> loader) throws ExecutionException {
		final ListenableFuture<V> loadingFuture = loadFuture(key, loader);
		return loadAndRecordStats(key, loadingFuture, Stopwatch.createStarted());
	}

	private ListenableFuture<V> loadAsync(final K key, CacheLoader<K, V> loader) {
		final ListenableFuture<V> loadingFuture = loadFuture(key, loader);
		loadingFuture.addListener(() -> {
			try {
				loadAndRecordStats(key, loadingFuture, Stopwatch.createStarted());
			} catch (Throwable t) {
				logger.log(Level.WARNING, "Exception thrown during refresh", t);
			}
		}, directExecutor());
		return loadingFuture;
	}

	private V loadAndRecordStats(K key, ListenableFuture<V> newValue, Stopwatch stopwatch) throws ExecutionException {
		V value = null;
		try {
			value = getUninterruptibly(newValue);
			if(value == null) {
				throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
			}
			stats.recordLoadSuccess(stopwatch.elapsed(NANOSECONDS));
			put(key, value);
			return value;
		} finally {
			if(value == null) {
				stats.recordLoadException(stopwatch.elapsed(NANOSECONDS));
			}
		}
	}

	private ListenableFuture<V> loadFuture(K key, CacheLoader<K, V> loader) {
		try {
			final V previousValue = getIfPresent(key);
			if(previousValue == null) {
				return Futures.immediateFuture(loader.load(key));
			}
			ListenableFuture<V> newValue = loader.reload(key, previousValue);
			return newValue == null ? Futures.immediateFuture(null) : newValue;
		} catch(Throwable t) {
			if(t instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			return Futures.immediateFailedFuture(t);
		}
	}

	protected static ExecutionException convertAndThrow(Throwable t) throws ExecutionException {
		if(t instanceof InterruptedException) {
			Thread.currentThread().interrupt();
			throw new ExecutionException(t);
		} else if(t instanceof RuntimeException) {
			throw new UncheckedExecutionException(t);
		} else if(t instanceof Exception) {
			throw new ExecutionException(t);
		} else {
			throw new ExecutionError((Error) t);
		}
	}
}
