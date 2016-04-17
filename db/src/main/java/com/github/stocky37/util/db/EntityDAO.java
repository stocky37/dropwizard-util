package com.github.stocky37.util.db;

import io.dropwizard.hibernate.AbstractDAO;
import com.github.stocky37.util.core.Service;
import org.hibernate.SessionFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Note: Entity MUST be the first generic type of any super classes, since AbstractDAO
// calls getGenericType(), which will always retrieve the first generic type attached
@ParametersAreNonnullByDefault
public abstract class EntityDAO<E, T, U, I extends Serializable> extends AbstractDAO<E>
	implements Service<T, U, I>, EntityTransformer<T, U, E> {

	public EntityDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Nonnull
	protected abstract String listAllNamedQuery();

	@Override
	public List<T> list() {
		return list(namedQuery(listAllNamedQuery())).stream()
			.map(this::from)
			.collect(Collectors.toList());
	}

	@Override
	public T create(T entity) {
		currentSession().save(to(entity));
		return entity;
	}

	@Override
	public Optional<T> find(I id) {
		return Optional.ofNullable(from(get(id)));
	}

	@Override
	public Optional<T> update(I id, U update) {
		@Nullable final E existing = get(id);
		if(existing == null) {
			return Optional.empty();
		}
		final E updated = merge(existing, update);
		currentSession().update(updated);
		return Optional.ofNullable(from(updated));
	}

	@Override
	public Optional<T> delete(I id) {
		@Nullable final E existing = get(id);
		if(existing != null) {
			currentSession().delete(existing);
		}
		return Optional.ofNullable(from(existing));
	}
}
