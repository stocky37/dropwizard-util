package me.stockwells.util.core;

import me.stockwells.util.api.resources.GenericResource;
import me.stockwells.util.api.resources.GenericService;
import com.google.common.base.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.util.Collection;


public abstract class EntityResource<T, U, I extends Serializable> implements GenericResource<T, U, I> {

	@Context private UriInfo uri;
	@Context private HttpServletResponse response;

	private final GenericService<T, U, I> service;

	public EntityResource(GenericService<T, U, I> service) {
		this.service = service;
	}

	@Override
	public Collection<T> all() {
		return service.all();
	}

	@Override
	public T create(T value) {
		T created = service.create(value);
		if(created != null) {
			response.setStatus(Response.Status.CREATED.getStatusCode());
			response.addHeader(HttpHeaders.LOCATION, uri.getAbsolutePathBuilder()
					.path(String.valueOf(id(created)))
					.build().toString()
			);
			try {
				response.flushBuffer();
			}catch(Exception e){}
		}
		return created;
	}

	@Override
	public Optional<T> find(I id) {
		return Optional.fromNullable(service.findById(id).orElse(null));
	}

	@Override
	public Optional<T> update(I id, U updated) {
		return Optional.fromNullable(service.update(id, updated).orElse(null));
	}

	@Override
	public Optional<T> delete(I id) {
		return Optional.fromNullable(service.delete(id).orElse(null));
	}

	protected abstract I id(T obj);
}
