package me.stockwells.util.example;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CreatedResourceImpl implements CreatedResource {
	public static final class TestJsonRepr {
		@JsonProperty private String key;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}


	@Override
	public TestJsonRepr created() {
		TestJsonRepr repr = new TestJsonRepr();
		repr.setKey("testing");
		return repr;
	}

	@Override
	public TestJsonRepr wrongcreated() {
		TestJsonRepr repr = new TestJsonRepr();
		repr.setKey("testing");
		return repr;
	}

	@Override
	public TestJsonRepr notCreated() {
		TestJsonRepr repr = new TestJsonRepr();
		repr.setKey("testing");
		return repr;
	}
}
