package me.stockwells.util.db;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public interface EntityTransformer<T, U, E> {
	E to(T type);
	@Nullable T from(@Nullable E entity);
	E merge(E entity, U updated);
}
