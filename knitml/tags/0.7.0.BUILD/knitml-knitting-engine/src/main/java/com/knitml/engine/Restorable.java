package com.knitml.engine;

public interface Restorable {
	
	Object save();
	void restore(Object memento);

}
