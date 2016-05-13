package com.github.stocky37.util.core.paging;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PageValueFactoryProvider extends AbstractValueFactoryProvider {

	@Inject
	public PageValueFactoryProvider(MultivaluedParameterExtractorProvider mpep, ServiceLocator locators) {
		super(mpep, locators, Parameter.Source.UNKNOWN);
	}

	@Override
	protected Factory<Page> createValueFactory(Parameter parameter) {
		return parameter.isAnnotationPresent(PagingParam.class) && parameter.getRawType().isAssignableFrom(Page.class)
			? new PageContainerRequestValueFactory(new PageParamFactory(parameter.getAnnotation(PagingParam.class)))
			: null;
	}

	@Singleton
	static final class InjectionResolver extends ParamInjectionResolver<PagingParam> {
		public InjectionResolver() {
			super(PageValueFactoryProvider.class);
		}
	}

	static class PageContainerRequestValueFactory extends AbstractContainerRequestValueFactory<Page> {
		private final PageParamFactory pageParamFactory;

		PageContainerRequestValueFactory(PageParamFactory pageParamFactory) {
			this.pageParamFactory = pageParamFactory;
		}

		@Override
		public Page provide() {
			return pageParamFactory.build(getContainerRequest());
		}
	}
}
