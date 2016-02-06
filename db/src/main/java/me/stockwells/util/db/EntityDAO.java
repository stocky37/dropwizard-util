package me.stockwells.util.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;


// Note: Entity MUST be the first generic type of any super classes, since AbstractDAO
// calls getGenericType(), which will always retrieve the first generic type attached
public abstract class EntityDAO<E, I extends Serializable> extends AbstractDAO<E> implements GenericDAO<E, I> {

	public EntityDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Nonnull protected abstract String getAllNamedQuery();

	@Override
	public List<E> all() {
		return list(namedQuery(getAllNamedQuery()));
	}

	@Override
	public Optional<E> findById(I id) {
		return Optional.ofNullable(get(id));
	}

	@Override
	public E create(E entity) {
		currentSession().save(entity);
		return entity;
	}

	@Override
	public E update(E update) {
		currentSession().update(update);
		return update;
	}

	@Override
	public E delete(E entity) {
		currentSession().delete(checkNotNull(entity));
		return entity;
	}
}
