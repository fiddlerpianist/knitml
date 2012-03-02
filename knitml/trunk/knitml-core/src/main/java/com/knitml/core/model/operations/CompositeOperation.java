package com.knitml.core.model.operations;

import java.util.List;

public interface CompositeOperation {
	List<? extends Operation> getOperations();
}
