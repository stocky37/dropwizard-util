package com.github.stocky37.util.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.ogm.OgmSessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractResourceDAO<E, I extends Serializable> extends AbstractDAO<E> implements ResourceDAO<E, I> {

	public AbstractResourceDAO(OgmSessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<E> listAll() {
		return list(namedQuery(nameOfListAllQuery()));
	}

	@Override
	public E create(E entity) {
		currentSession().save(entity);
		return entity;
	}

	@Override
	public Optional<E> find(I id) {
		return Optional.ofNullable(get(id));
	}

	@Override
	public Optional<E> delete(I id) {
		final Optional<E> entity = find(id);
		entity.ifPresent(e -> currentSession().delete(e));
		return entity;
	}

	@Override
	public E update(E updated) {
		currentSession().update(updated);
		return updated;
	}

	protected abstract String nameOfListAllQuery();
}
