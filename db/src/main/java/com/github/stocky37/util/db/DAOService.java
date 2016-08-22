package com.github.stocky37.util.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.github.stocky37.util.core.AbstractService;
import com.google.common.base.Converter;
import com.google.common.base.Throwables;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class DAOService<T, E, I extends Serializable> extends AbstractService<T, I> {
	private final ResourceDAO<E, I> dao;
	private final Converter<T, E> converter;

	public DAOService(ResourceDAO<E, I> dao, Converter<T, E> converter) {
		this.dao = dao;
		this.converter = converter;
	}

	public DAOService(ResourceDAO<E, I> dao, Converter<T, E> converter, ObjectMapper mapper) {
		super(mapper);
		this.dao = dao;
		this.converter = converter;
	}

	public ResourceDAO<E, I> getDao() {
		return dao;
	}

	public Converter<T, E> getConverter() {
		return converter;
	}

	@Override
	public List<T> list() {
		return dao.listAll().stream()
			.map(e -> converter.reverse().convert(e))
			.collect(Collectors.toList());
	}

	@Override
	public T create(T obj) {
		return converter.reverse().convert(dao.create(converter.convert(obj)));
	}

	@Override
	public Optional<T> find(I id) {
		return dao.find(id).map(e -> converter.reverse().convert(e));
	}

	@Override
	public Optional<T> delete(I id) {
		return dao.delete(id).map(e -> converter.reverse().convert(e));
	}

	@Override
	public Optional<T> update(I id, JsonMergePatch patch) {
		final Optional<E> existing = dao.find(id);
		if(!existing.isPresent()) {
			return Optional.empty();
		}
		try {
			final E entity = existing.get();
			final E patched = converter.convert(patch(converter.reverse().convert(entity), patch));
			return Optional.ofNullable(converter.reverse().convert(dao.update(copy(entity, patched))));
		} catch(JsonPatchException e) {
			throw Throwables.propagate(e);
		}
	}

	protected abstract E copy(E entity, E patched);
}
