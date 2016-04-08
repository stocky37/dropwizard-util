package me.stockwells.util.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;


public interface DAO<E, I extends Serializable> {
	E create(E entity);
	Optional<E> findById(I id);
	E update(E entity);
	E delete(E entity);
	Collection<E> all();
}
