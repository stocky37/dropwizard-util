package com.github.stocky37.util.nosqldb;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.ogm.OgmSession;
import org.hibernate.ogm.OgmSessionFactory;

public abstract class OgmAbstractDAO<E> extends AbstractDAO<E> {
	public OgmAbstractDAO(OgmSessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected OgmSession currentSession() {
		return (OgmSession)super.currentSession();
	}
}
