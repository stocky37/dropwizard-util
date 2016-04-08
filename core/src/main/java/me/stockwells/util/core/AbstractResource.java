package me.stockwells.util.core;

import me.stockwells.util.api.resources.Resource;

import java.util.Optional;

public abstract class AbstractResource<T, U> implements Resource<T, U> {
	private final Service<T,U> service;

	public AbstractResource(Service<T, U> service) {
		this.service = service;
	}

	@Override
	public Optional<T> get() {
		return service.get();
	}

	@Override
	public Optional<T> update(U update) {
		return service.update(update);
	}

	@Override
	public Optional<T> delete() {
		return service.delete();
	}
}
