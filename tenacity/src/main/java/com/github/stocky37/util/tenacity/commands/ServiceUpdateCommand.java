package com.github.stocky37.util.tenacity.commands;

import com.yammer.tenacity.core.TenacityCommand;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import com.github.stocky37.util.core.Service;

import java.util.Optional;

public class ServiceUpdateCommand<T, U, I> extends TenacityCommand<Optional<T>> {

	private final Service<T, U, I> service;
	private final I id;
	private final U update;

	public ServiceUpdateCommand(TenacityPropertyKey key, Service<T, U, I> service, I id, U update) {
		super(key);
		this.service = service;
		this.id = id;
		this.update = update;
	}

	@Override
	protected Optional<T> run() throws Exception {
		return service.update(id, update);
	}
}
