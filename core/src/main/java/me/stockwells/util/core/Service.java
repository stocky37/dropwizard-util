package me.stockwells.util.core;

import javax.validation.Valid;
import java.util.Optional;

public interface Service<T, U> {
	Optional<T> get();
	Optional<T> update(@Valid U update);
	Optional<T> delete();
}
