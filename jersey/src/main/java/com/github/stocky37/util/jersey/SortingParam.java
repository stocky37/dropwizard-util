package com.github.stocky37.util.jersey;

import io.dropwizard.jersey.params.AbstractParam;

public class SortingParam extends AbstractParam<Sorting> {
	public SortingParam(String input) {
		super(input);
	}

	@Override
	protected Sorting parse(String input) throws Exception {
		return Sorting.from(input);
	}
}
