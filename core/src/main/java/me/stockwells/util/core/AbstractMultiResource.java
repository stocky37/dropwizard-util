package me.stockwells.util.core;

import me.stockwells.util.api.resources.MultiResource;

import java.io.Serializable;
import java.util.Collection;

public abstract class AbstractMultiResource<T, I extends Serializable> implements MultiResource<T, I> {

	private final MultiService<T, I> service;

	public AbstractMultiResource(MultiService<T, I> service) {
		this.service = service;
	}

	@Override
	public Collection<T> all() {
		return service.all();
	}

	@Override
	public T add(T obj) {
		return service.add(obj);
	}
}
