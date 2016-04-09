package me.stockwells.util.tenacity.commands;

import com.yammer.tenacity.core.TenacityCommand;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import me.stockwells.util.core.Service;

import java.io.Serializable;
import java.util.Optional;

public class ServiceFindCommand<T, I> extends TenacityCommand<Optional<T>> {

	private final Service<T, ?, I> service;
	private final I id;

	public ServiceFindCommand(TenacityPropertyKey key, Service<T, ?, I> service, I id) {
		super(key);
		this.service = service;
		this.id = id;
	}

	@Override
	protected Optional<T> run() throws Exception {
		return service.find(id);
	}
}
