package com.github.stocky37.util.tenacity.commands;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.yammer.tenacity.core.TenacityCommand;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import com.github.stocky37.util.core.Service;

import java.util.Optional;

public class ServiceUpdateCommand<T, I> extends TenacityCommand<Optional<T>> {

	private final Service<T, I> service;
	private final I id;
	private final JsonMergePatch patch;

	public ServiceUpdateCommand(TenacityPropertyKey key, Service<T, I> service, I id, JsonMergePatch patch) {
		super(key);
		this.service = service;
		this.id = id;
		this.patch = patch;
	}

	@Override
	protected Optional<T> run() throws Exception {
		return service.update(id, patch);
	}
}
