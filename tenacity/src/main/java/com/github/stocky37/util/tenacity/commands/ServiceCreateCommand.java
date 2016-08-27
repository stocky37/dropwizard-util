package com.github.stocky37.util.tenacity.commands;

import com.github.stocky37.util.core.Service;
import com.yammer.tenacity.core.TenacityCommand;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;

public class ServiceCreateCommand<T> extends TenacityCommand<T> {
	private final Service<T, ?> delegate;
	private final T entity;

	public ServiceCreateCommand(TenacityPropertyKey key, Service<T, ?> delegate, T resource) {
		super(key);
		this.delegate = delegate;
		this.entity = resource;
	}

	@Override
	protected T run() throws Exception {
		return delegate.create(entity);
	}
}
