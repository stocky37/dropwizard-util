package com.github.stocky37.util.core;

import com.github.stocky37.util.api.resources.Resource;

import java.util.Optional;

public class AbstractResource<T, U, I> implements Resource<T, U> {
	private final Service<T, U, I> service;
	private final I id;

	public AbstractResource(Service<T, U, I> service, I id) {
		this.service = service;
		this.id = id;
	}

	@Override
	public Optional<T> get() {
		return service.find(id);
	}

	@Override
	public Optional<T> update(U update) {
		return service.update(id, update);
	}

	@Override
	public Optional<T> delete() {
		return service.delete(id);
	}
}
