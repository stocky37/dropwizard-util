package me.stockwells.util.tenacity.commands;

import com.yammer.tenacity.core.TenacityCommand;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import me.stockwells.util.core.Service;

import java.util.List;

public class ServiceListCommand<T> extends TenacityCommand<List<T>> {

	private final Service<T,?,?> service;

	public ServiceListCommand(TenacityPropertyKey key, Service<T, ?, ?> service) {
		super(key);
		this.service = service;
	}

	@Override
	protected List<T> run() throws Exception {
		return service.list();
	}
}
