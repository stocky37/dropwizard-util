package com.github.stocky37.util.tenacity.commands;

import com.github.stocky37.util.core.Service;
import com.yammer.tenacity.core.TenacityCommand;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;

import java.util.List;

public class ServiceListCommand<T> extends TenacityCommand<List<T>> {

	private final Service<T, ?> service;

	public ServiceListCommand(TenacityPropertyKey key, Service<T, ?> service) {
		super(key);
		this.service = service;
	}

	@Override
	protected List<T> run() throws Exception {
		return service.list();
	}
}
