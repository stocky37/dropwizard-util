package com.github.stocky37.util.db.ogm.config;

import io.dropwizard.Configuration;
import org.hibernate.service.ServiceRegistry;

public interface NoSQLDatabaseConfiguration<T extends Configuration> {
	ServiceRegistry getServiceRegistry(T configuration);
}
