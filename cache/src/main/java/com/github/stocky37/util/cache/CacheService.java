package com.github.stocky37.util.cache;

import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.github.stocky37.util.core.AbstractService;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

public abstract class CacheService<T, I> extends AbstractService<T, I> {
	private final Cache<I, T> cache;

	public CacheService(Cache<I, T> cache) {
		this.cache = cache;
	}

	@Override
	public List<T> list() {
		return ImmutableList.copyOf(cache.asMap().values());
	}

	@Override
	public Optional<T> find(I id) {
		return Optional.ofNullable(cache.getIfPresent(id));
	}

	@Override
	public Optional<T> update(I id, JsonMergePatch patch) {
		try {
			return Optional.ofNullable(patch(find(id).orElse(null), patch));
		} catch(JsonPatchException e) {
			throw Throwables.propagate(e);
		}
	}

	@Override
	public Optional<T> delete(I id) {
		T existing = cache.getIfPresent(id);
		cache.invalidate(id);
		return Optional.ofNullable(existing);
	}

	@Override
	public T create(T obj) {
		cache.put(identify(obj), obj);
		return obj;
	}

	protected abstract I identify(T obj);
}
