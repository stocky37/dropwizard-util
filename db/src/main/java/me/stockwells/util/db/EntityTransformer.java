package me.stockwells.util.db;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public interface EntityTransformer<T, U, E> {
	@Nonnull E to(@Nonnull T type);
	@Nullable T from(@Nullable E entity);
	@Nonnull E merge(@Nonnull E entity, @Nonnull U updated);
}
