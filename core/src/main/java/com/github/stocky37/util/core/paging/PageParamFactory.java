package com.github.stocky37.util.core.paging;

import io.dropwizard.jersey.params.IntParam;

import javax.ws.rs.container.ContainerRequestContext;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class PageParamFactory {
	private final PagingParam paging;

	public PageParamFactory(PagingParam paging) {
		this.paging = paging;
	}

	public int getDefaultIndex() {
		return paging.defaultIndex() == -1 ? paging.strategy().getDefaultIndex() : paging.defaultIndex();
	}

	public String getIndexParam() {
		return isBlank(paging.indexParam()) ? paging.strategy().getDefaultIndexParam() : paging.indexParam();
	}

	public int getDefaultSize() {
		return paging.defaultSize() == -1 ? paging.strategy().getDefaultSize() : paging.defaultSize();
	}

	public String getSizeParam() {
		return isBlank(paging.sizeParam()) ? paging.strategy().getDefaultSizeParam() : paging.sizeParam();
	}

	public Page build(ContainerRequestContext request) {
		final int index = getDefaultIndex(request);
		final int size = getDefaultSize(request);
		switch(paging.strategy()) {
			case NUMBERED:
				return new NumberedPage(index, size);
			case OFFSET:
				return new OffsetPage(index, size);
			default:
				return null;
		}
	}

	private int getDefaultIndex(ContainerRequestContext request) {
		return getIntValue(
			request,
			getIndexParam(),
			getDefaultIndex()
		);
	}

	private int getDefaultSize(ContainerRequestContext request) {
		return getIntValue(
			request,
			getSizeParam(),
			getDefaultSize()
		);
	}

	private int getIntValue(ContainerRequestContext request, String param, int defaultValue) {
		final String paramValue = request.getUriInfo().getQueryParameters().getFirst(param);
		return isBlank(paramValue) ? defaultValue : new IntParam(paramValue).get();
	}
}
