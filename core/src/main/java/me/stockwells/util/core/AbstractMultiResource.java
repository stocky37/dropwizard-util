package me.stockwells.util.core;

import me.stockwells.util.api.resources.MultiResource;
import me.stockwells.util.api.resources.Resource;

import java.io.Serializable;
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
