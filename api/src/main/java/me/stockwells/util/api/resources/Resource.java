package me.stockwells.util.api.resources;

import javax.validation.Valid;
import java.util.Optional;

public interface Resource<T, U> {
	@Valid Optional<T> get();
	@Valid Optional<T> update(@Valid U update);
	@Valid Optional<T> delete();
}
