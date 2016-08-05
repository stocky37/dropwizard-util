package com.github.stocky37.util.ogm.config;

import io.dropwizard.Configuration;
import org.hibernate.service.ServiceRegistry;

public interface NoSQLDatabaseConfiguration<T extends Configuration> {
	ServiceRegistry getServiceRegistry(T configuration);
}
