package com.github.stocky37.util.paging;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@ParametersAreNonnullByDefault
public final class OffsetPage implements Page {
	private final int offset;
	private final int limit;

	public OffsetPage(int offset, int limit) {
		checkArgument(offset >= 0);
		checkArgument(limit > 0);
		this.offset = offset;
		this.limit = limit;
	}

	public static OffsetPage from(NumberedPage page) {
		return new OffsetPage((page.index() - 1) * page.size(), page.size());
	}

	@Override
	public int index() {
		return offset;
	}

	@Override
	public int size() {
		return limit;
	}

	@Override
	public Page first() {
		return new OffsetPage(0, limit);
	}

	@Override
	public Page last(int total) {
		return new OffsetPage((total - limit) + limit % offset, limit);
	}

	@Override
	public Optional<Page> previous() {
		if(offset == 0) {
			return Optional.empty();
		}
		final int newOffset = offset - limit;
		return Optional.of(new OffsetPage(newOffset < 0 ? 0 : newOffset, limit));
	}

	@Override
	public Optional<Page> next(int total) {
		final int newIndex = offset + limit;
		return Optional.ofNullable(newIndex >= total ? null : new OffsetPage(newIndex, limit));
	}

	@Override
	public int hashCode() {
		return Objects.hash(offset, limit);
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		OffsetPage that = (OffsetPage)o;
		return offset == that.offset
			&& limit == that.limit;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("offset", offset)
			.add("limit", limit)
			.toString();
	}
}
