package com.github.stocky37.util.jersey;

public @interface SourceParam {
	enum ParamType {
		PATH, QUERY, JSON
	}

	String key();
	ParamType type() default ParamType.PATH;
	String fallback() default "";
}
