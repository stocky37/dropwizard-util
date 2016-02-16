package me.stockwells.util.example.resources;

import com.google.common.base.Throwables;
import com.google.common.cache.LoadingCache;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutionException;

@Path("cached")
@Produces(MediaType.TEXT_PLAIN)
public class CachedResource {
	private final LoadingCache<String, String> cache;

	public CachedResource(LoadingCache<String, String> cache) {
		this.cache = cache;
	}

	@POST
	@Path("{key}/{value}")
	public void str(@PathParam("key") String key, @PathParam("value") String value) {
		cache.put(key, value);
	}

	@GET
	@Path("{key}")
	public String str(@PathParam("key") String key) {
		try {
			return cache.get(key);
		} catch(ExecutionException e) {
			throw Throwables.propagate(e);
		}
	}
}
