package com.github.stocky37.util.ogm.config;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("fongo")
public class FongoDataSource extends OgmDataSource {
	@Override
	public String getDatastoreProvider() {
		return "fongo";
	}
}
