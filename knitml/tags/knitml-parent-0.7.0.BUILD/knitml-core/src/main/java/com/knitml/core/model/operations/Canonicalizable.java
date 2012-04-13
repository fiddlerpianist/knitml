package com.knitml.core.model.operations;

import java.util.List;

public interface Canonicalizable {
	List<? extends Operation> canonicalize();
}
