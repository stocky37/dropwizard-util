package me.stockwells.util.api.resources;


import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;


public interface GenericService<T, U, I extends Serializable> {
	Collection<T> all();
	T create(T value);
	Optional<T> findById(I id);
	Optional<T> update(I id, U value);
	Optional<T> delete(I id);
}
