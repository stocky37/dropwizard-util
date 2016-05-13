package com.github.stocky37.util.core.paging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface PagingParam {
	PagingStrategy strategy() default PagingStrategy.NUMBERED;

	// if set, these act as overrides of the enum default values
	String indexParam() default "";
	String sizeParam() default "";
	int defaultIndex() default -1;
	int defaultSize() default -1;

	enum PagingStrategy {
		NUMBERED("page", "pageSize", 1, 30),
		OFFSET("offset", "limit", 0, 30);

		private final String defaultIndexParam;
		private final String defaultSizeParam;
		private final int defaultIndex;
		private final int defaultSize;

		PagingStrategy(String defaultIndexParam, String defaultSizeParam, int defaultIndex, int defaultSize) {
			this.defaultIndex = defaultIndex;
			this.defaultSize = defaultSize;
			this.defaultIndexParam = defaultIndexParam;
			this.defaultSizeParam = defaultSizeParam;
		}

		public int getDefaultIndex() {
			return defaultIndex;
		}

		public int getDefaultSize() {
			return defaultSize;
		}

		public String getDefaultIndexParam() {
			return defaultIndexParam;
		}

		public String getDefaultSizeParam() {
			return defaultSizeParam;
		}
	}
}
