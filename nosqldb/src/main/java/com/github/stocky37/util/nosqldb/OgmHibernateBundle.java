package com.github.stocky37.util.nosqldb;


import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.github.stocky37.util.nosqldb.config.NoSQLDatabaseConfiguration;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.ogm.OgmSessionFactory;
import org.hibernate.ogm.boot.OgmSessionFactoryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class OgmHibernateBundle<T extends Configuration>
	implements ConfiguredBundle<T>, NoSQLDatabaseConfiguration<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(OgmHibernateBundle.class);

	private final Collection<Class<?>> entities;

	private OgmSessionFactory sessionFactory;
	private boolean lazyLoadingEnabled = true;

	protected OgmHibernateBundle(Class<?> entity, Class<?>... entities) {
		this(ImmutableList.<Class<?>>builder().add(entity).add(entities).build());
	}

	protected OgmHibernateBundle(Collection<Class<?>> entities) {
		this.entities = ImmutableList.copyOf(entities);
	}

	@Override
	public final void initialize(Bootstrap<?> bootstrap) {
		bootstrap.getObjectMapper().registerModule(createHibernate5Module());
	}

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		final ServiceRegistry registry = getServiceRegistry(configuration);
		sessionFactory = addAnnotatedClasses(new MetadataSources(registry))
			.buildMetadata()
			.getSessionFactoryBuilder()
			.unwrap(OgmSessionFactoryBuilder.class)
			.build();
	}

	public OgmSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public boolean isLazyLoadingEnabled() {
		return lazyLoadingEnabled;
	}

	public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
		this.lazyLoadingEnabled = lazyLoadingEnabled;
	}

	/**
	 * Override to configure the {@link Hibernate5Module}.
	 */
	protected Hibernate5Module createHibernate5Module() {
		Hibernate5Module module = new Hibernate5Module();
		if(lazyLoadingEnabled) {
			module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
		}
		return module;
	}

	private MetadataSources addAnnotatedClasses(MetadataSources metadata) {
		final SortedSet<String> entityClasses = new TreeSet<>();
		for (Class<?> klass : entities) {
			metadata.addAnnotatedClass(klass);
			entityClasses.add(klass.getCanonicalName());
		}
		LOGGER.info("Entity classes: {}", entityClasses);
		return metadata;
	}

}
