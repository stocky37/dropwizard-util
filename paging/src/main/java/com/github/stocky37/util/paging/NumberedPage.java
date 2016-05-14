package com.github.stocky37.util.paging;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@ParametersAreNonnullByDefault
public final class NumberedPage implements Page {
	private final int number;
	private final int size;

	public NumberedPage(int number, int size) {
		checkArgument(number > 0);
		checkArgument(size > 0);
		this.number = number;
		this.size = size;
	}

	@Override
	public int index() {
		return number;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Page first() {
		return new NumberedPage(1, size);
	}

	@Override
	public Page last(int total) {
		return new NumberedPage(lastPage(total), size);
	}

	@Override
	public Optional<Page> previous() {
		return Optional.ofNullable(number <= 1 ? null : new NumberedPage(number - 1, size));
	}

	@Override
	public Optional<Page> next(int total) {
		return Optional.ofNullable(number >= lastPage(total) ? null : new NumberedPage(number + 1, size));
	}

	private int lastPage(int total) {
		return total / size + 1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(number, size);
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		NumberedPage that = (NumberedPage)o;
		return number == that.number
			&& size == that.size;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("number", number)
			.add("size", size)
			.toString();
	}
}
