package com.github.stocky37.util.core;

import com.github.stocky37.util.api.resources.Resources;
import com.github.stocky37.util.api.resources.Resource;

import java.util.Collection;
import java.util.List;

public abstract class AbstractResources<T, I> implements Resources<T, I> {
	private final Service<T, I> service;
	private final ResourceFactory<T, I> resourceFactory;

	public AbstractResources(Service<T, I> service, ResourceFactory<T, I> resourceFactory) {
		this.service = service;
		this.resourceFactory = resourceFactory;
	}

	public Service<T, I> getService() {
		return service;
	}

	public ResourceFactory<T, I> getResourceFactory() {
		return resourceFactory;
	}

	@Override
	public List<T> list() {
		return service.list();
	}

	@Override
	public T create(T obj) {
		return service.create(obj);
	}

	@Override
	public Resource<T> find(I id) {
		return resourceFactory.build(id);
	}
}
