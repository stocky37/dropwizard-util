package com.github.stocky37.util.ogm;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.ogm.OgmSession;
import org.hibernate.ogm.OgmSessionFactory;

import java.io.Serializable;
import java.util.Optional;

public abstract class OgmAbstractDAO<E> extends AbstractDAO<E> {
	public OgmAbstractDAO(OgmSessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected OgmSession currentSession() {
		return (OgmSession)super.currentSession();
	}

	protected Optional<E> findById(String query, Serializable id) {
		return Optional.ofNullable(uniqueResult(namedQuery(query).setParameter("id", id)));
	}
}
