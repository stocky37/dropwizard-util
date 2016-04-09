package me.stockwells.util.core;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Service<T, U, I> {
	List<T> list();
	T create(T obj);
	Optional<T> find(I id);
	Optional<T> update(I id, @Valid U update);
	Optional<T> delete(I id);
}
