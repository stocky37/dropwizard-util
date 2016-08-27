package com.github.stocky37.util.tenacity;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.github.stocky37.util.core.Service;

import java.util.List;
import java.util.Optional;

public class HystrixService<T, I> implements Service<T, I> {

	private final ServiceCommandFactory<T, I> commands;

	public HystrixService(ServiceCommandFactory<T, I> commands) {
		this.commands = commands;
	}

	@Override
	public List<T> list() {
		return commands.list().execute();
	}

	@Override
	public T create(T obj) {
		return commands.create(obj).execute();
	}

	@Override
	public Optional<T> find(I id) {
		return commands.find(id).execute();
	}

	@Override
	public Optional<T> update(I id, JsonMergePatch patch) {
		return commands.update(id, patch).execute();
	}

	@Override
	public Optional<T> delete(I id) {
		return commands.delete(id).execute();
	}
}
