package me.stockwells.util.example.resources;

import me.stockwells.util.example.representations.Example;
import me.stockwells.util.example.api.CreatedResource;


public class CreatedResourceImpl implements CreatedResource {
	private final Example example;

	public CreatedResourceImpl(Example example) {
		this.example = example;
	}

	@Override
	public Example created() {
		return example;
	}

	@Override
	public Example invalid() {
		return example;
	}
}
