package com.github.stocky37.util.cache;

import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

@ParametersAreNonnullByDefault
public abstract class StandardLoadingCache<K, V> extends AbstractLoadingCache<K, V> {
	private static final Logger logger = Logger.getLogger(StandardLoadingCache.class.getName());

	private final CacheLoader<K, V> loader;

	public StandardLoadingCache(CacheLoader<K, V> loader) {
		this.loader = loader;
	}

	@Override
	public V get(@Nonnull K key) throws ExecutionException {
		return getFromFuture(key, loadFuture(key, loader));
	}

	@Override
	public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
		return getFromFuture(key, loadFuture(key, new CacheLoader<K, V>() {
			@Override
			public V load(K key) throws Exception {
				return valueLoader.call();
			}
		}));
	}

	@Override
	public void refresh(K key) {
		final ListenableFuture<V> loadingFuture = loadFuture(key, loader);
		loadingFuture.addListener(() -> {
			try {
				put(key, getFromFuture(key, loadingFuture));
			} catch(Throwable t) {
				logger.log(Level.WARNING, "Exception thrown during refresh", t);
			}
		}, MoreExecutors.directExecutor());
	}

	private V getFromFuture(K key, Future<V> newValue) throws ExecutionException {
		return Optional.ofNullable(Uninterruptibles.getUninterruptibly(newValue))
			.orElseThrow(() -> new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + "."));
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
