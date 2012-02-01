package com.knitml.validation.context;

public interface ListenerManager {
	void addListener(PatternEventListener listener);
	void fireBegin(Object event, KnittingContext context);
	void fireEnd(Object event, KnittingContext context);
}
