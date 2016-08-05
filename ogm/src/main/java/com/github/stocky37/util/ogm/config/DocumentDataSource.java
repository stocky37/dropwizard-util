package com.github.stocky37.util.ogm.config;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.ogm.datastore.document.cfg.DocumentStoreProperties;
import org.hibernate.ogm.datastore.document.options.AssociationStorageType;
import org.hibernate.ogm.datastore.document.options.MapStorageType;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class DocumentDataSource extends OgmDataSource {

	private Optional<AssociationStorageType> associationStorage = Optional.empty();
	private Optional<MapStorageType> mapStorage = Optional.empty();

	@Override
	protected StandardServiceRegistryBuilder builder() {
		final StandardServiceRegistryBuilder builder = super.builder();
		associationStorage.ifPresent(x -> builder.applySetting(DocumentStoreProperties.ASSOCIATIONS_STORE, x));
		mapStorage.ifPresent(x -> builder.applySetting(DocumentStoreProperties.MAP_STORAGE, x));
		return builder;
	}
}
