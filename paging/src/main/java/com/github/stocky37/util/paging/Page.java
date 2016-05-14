package com.github.stocky37.util.paging;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public interface Page {
	int index();
	int size();
	Page first();
	Page last(int total);
	Optional<Page> previous();
	Optional<Page> next(int total);
}
