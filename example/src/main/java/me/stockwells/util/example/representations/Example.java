package me.stockwells.util.example.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;


@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Example {

	@ApiModelProperty("A random string value")
	private final String value;

	@JsonCreator
	public Example(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof Example)) return false;
		final Example that = (Example)o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("value", value)
			.toString();
	}
}
