package me.stockwells.util.api.resources;

import com.google.common.base.Optional;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;


public interface GenericResource<T, U, I extends Serializable> {
	String ID_PARAM = "id";
	String ID_PATH = "{" + ID_PARAM + "}";

	@Valid Collection<T> all();
	@Valid T create(T value);
	@Valid Optional<T> find(I id);
	@Valid Optional<T> update(I id, @Valid U value);
	@Valid Optional<T> delete(I id);
}
