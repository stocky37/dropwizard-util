package com.github.stocky37.util.db;

import org.hibernate.ogm.OgmSession;
import org.hibernate.ogm.OgmSessionFactory;

import java.io.Serializable;

public abstract class OgmResourceDAO<E, I extends Serializable> extends AbstractResourceDAO<E, I> {
	public static final String ID_PARAM = "id";

	public OgmResourceDAO(OgmSessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected OgmSession currentSession() {
		return (OgmSession)super.currentSession();
	}

	// Note: this is only required for the apparent bug in ogm where get() with polymorphic types
	//       will only return the parameters required for the base type and not the implementing types
	@Override
	protected E get(Serializable id) {
		return uniqueResult(namedQuery(nameOfFindQuery()).setParameter(ID_PARAM, id));
	}

	protected abstract String nameOfFindQuery();
}
