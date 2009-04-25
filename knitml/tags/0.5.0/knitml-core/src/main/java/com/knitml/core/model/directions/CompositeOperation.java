package com.knitml.core.model.directions;

import java.util.List;




public interface CompositeOperation {
	List<? extends Operation> getOperations();
}
