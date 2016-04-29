package com.github.stocky37.util.jersey;

public @interface LocationParam {
	String key();
	SourceParam source();
	SourceParam.ParamType type() default SourceParam.ParamType.PATH;
}
