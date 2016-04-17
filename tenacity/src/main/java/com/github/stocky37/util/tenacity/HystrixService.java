package com.github.stocky37.util.tenacity;

import com.github.stocky37.util.core.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public class HystrixService<T, U, I> implements Service<T, U, I> {

	private final ServiceCommandFactory<T, U, I> commands;

	public HystrixService(ServiceCommandFactory<T, U, I> commands) {
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
	public Optional<T> update(I id, @Valid U update) {
		return commands.update(id, update).execute();
	}

	@Override
	public Optional<T> delete(I id) {
		return commands.delete(id).execute();
	}
}
