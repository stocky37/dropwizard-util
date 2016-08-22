package com.github.stocky37.util.core;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.github.stocky37.util.api.resources.Resource;

import java.io.Serializable;
import java.util.Optional;

public class AbstractResource<T, I> implements Resource<T> {
	private final Service<T, I> service;
	private final I id;

	public AbstractResource(Service<T, I> service, I id) {
		this.service = service;
		this.id = id;
	}

	@Override
	public Optional<T> get() {
		return service.find(id);
	}

	@Override
	public Optional<T> update(JsonMergePatch patch) {
		return service.update(id, patch);
	}

	@Override
	public Optional<T> delete() {
		return service.delete(id);
	}
}
