package me.stockwells.util.core;

import me.stockwells.util.api.resources.GenericService;
import me.stockwells.util.db.EntityTransformer;
import me.stockwells.util.db.GenericDAO;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


public abstract class EntityService<T, U, E, I extends Serializable>
	implements GenericService<T, U, I>, EntityTransformer<T, U, E> {

	private final GenericDAO<E, I> dao;

	public EntityService(GenericDAO<E, I> dao) {
		this.dao = dao;
	}

	@Override
	public Collection<T> all() {
		return dao.all().stream().map(this::from).collect(Collectors.toList());
	}

	@Override
	public T create(T value) {
		return from(dao.create(to(value)));
	}

	@Override
	public Optional<T> findById(I id) {
		return Optional.ofNullable(from(dao.findById(id).orElseGet(null)));
	}

	@Override
	public Optional<T> update(I id, U value) {
		Optional<E> entity = dao.findById(id);
		if(!entity.isPresent()) {
			return Optional.empty();
		}
		return Optional.ofNullable(from(dao.update(merge(entity.get(), value))));
	}

	@Override
	public Optional<T> delete(I id) {
		Optional<E> entity = dao.findById(id);
		if(!entity.isPresent()) {
			return Optional.empty();
		}
		return Optional.ofNullable(from(dao.delete(entity.get())));
	}
}