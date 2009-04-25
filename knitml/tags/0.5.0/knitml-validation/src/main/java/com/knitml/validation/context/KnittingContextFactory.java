package com.knitml.validation.context;






public interface KnittingContextFactory {
	
	KnittingContext createKnittingContext();
	void shutdown();

}
