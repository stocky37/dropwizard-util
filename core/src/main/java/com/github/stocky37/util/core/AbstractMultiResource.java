package com.github.stocky37.util.core;

import com.github.stocky37.util.api.resources.MultiResource;
import com.github.stocky37.util.api.resources.Resource;

import java.util.Collection;

public class AbstractMultiResource<T, U, I> implements MultiResource<T, I> {

	private final Service<T, U, I> service;

	public AbstractMultiResource(Service<T, U, I> service) {
		this.service = service;
	}

	@Override
	public Collection<T> list() {
		return service.list();
	}

	@Override
	public T create(T obj) {
		return service.create(obj);
	}

	@Override
	public Resource<T, U> find(I id) {
		return new AbstractResource<>(service, id);
	}
}
