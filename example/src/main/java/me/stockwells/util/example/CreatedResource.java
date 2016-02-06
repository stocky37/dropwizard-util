package me.stockwells.util.example;

import me.stockwells.util.jersey.CREATED;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.POST;
import javax.ws.rs.Path;


@Path("created")
public class CreatedResource {
	public static final class TestJsonRepr {
		@JsonProperty private String key;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}


	@POST
	@CREATED("key")
	public TestJsonRepr created() {
		TestJsonRepr repr = new TestJsonRepr();
		repr.setKey("testing");
		return repr;
	}

	@Path("not")
	@POST
	public TestJsonRepr notCreated() {
		TestJsonRepr repr = new TestJsonRepr();
		repr.setKey("testing");
		return repr;
	}
}
