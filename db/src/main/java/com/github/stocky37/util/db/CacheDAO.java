package com.github.stocky37.util.db;

import com.google.common.cache.Cache;
import com.github.stocky37.util.core.Service;

import java.util.List;
import java.util.Optional;

public abstract class CacheDAO<T, I> implements Service<T, T, I> {

	private final Cache<I, T> cache;

	public CacheDAO(Cache<I, T> cache) {
		this.cache = cache;
	}

	@Override
	public List<T> list() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<T> find(I id) {
		return Optional.ofNullable(cache.getIfPresent(id));
	}

	@Override
	public Optional<T> update(I id, T update) {
		if(cache.getIfPresent(id) == null) {
			return Optional.empty();
		}
		cache.put(id, update);
		return Optional.of(update);
	}

	@Override
	public Optional<T> delete(I id) {
		T existing = cache.getIfPresent(id);
		cache.invalidate(id);
		return Optional.ofNullable(existing);
	}

	@Override
	public T create(T obj) {
		cache.put(getId(obj), obj);
		return obj;
	}

	protected abstract I getId(T obj);
}
