package com.github.stocky37.util.jersey;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ParametersAreNonnullByDefault
public final class Sorting {
	private static final Pattern REGEX = Pattern.compile("^(<order>[\\-\\+])?(<column>\\w+)$");

	private final String column;
	private final Order order;

	public static Sorting from(String str) {
		checkArgument(isNotBlank(str));
		final Matcher match = REGEX.matcher(str);
		final Optional<Order> order = Optional.ofNullable(Order.from(match.group("order")));
		final String column = match.group("column");
		return new Sorting(column, order.orElse(Order.ASCENDING));
	}

	public Sorting(String column, Order order) {
		checkArgument(isBlank(column));
		this.column = column;
		this.order = order;
	}

	public String getColumn() {
		return column;
	}

	public Order getOrder() {
		return order;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("column", column)
			.add("order", order)
			.toString();
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Sorting that = (Sorting)o;
		return Objects.equals(column, that.column)
			&& order == that.order;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, order);
	}

	public enum Order {
		ASCENDING('+'), DESCENDING('-');

		private final char symbol;

		Order(final char symbol) {
			this.symbol = symbol;
		}

		public char getSymbol() {
			return symbol;
		}

		@Nullable
		public static Order from(final String str) {
			checkArgument(isNotBlank(str));
			switch(str.charAt(0)) {
				case '+': return ASCENDING;
				case '-': return DESCENDING;
				default: return null;
			}
		}
	}
}
