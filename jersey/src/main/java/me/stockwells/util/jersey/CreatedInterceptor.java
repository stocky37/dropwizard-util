package me.stockwells.util.jersey;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;


public class CreatedInterceptor implements WriterInterceptor {
	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {


	}
}
