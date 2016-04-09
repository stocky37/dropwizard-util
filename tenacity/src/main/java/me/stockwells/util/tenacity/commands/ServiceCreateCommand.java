package me.stockwells.util.tenacity.commands;

import com.yammer.tenacity.core.TenacityCommand;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import me.stockwells.util.core.Service;

public class ServiceCreateCommand<T> extends TenacityCommand<T> {
	private final Service<T, ?, ?> service;
	private final T entity;

	public ServiceCreateCommand(TenacityPropertyKey key, Service<T, ?, ?> service, T entity) {
		super(key);
		this.service = service;
		this.entity = entity;
	}

	@Override
	protected T run() throws Exception {
		return service.create(entity);
	}
}
