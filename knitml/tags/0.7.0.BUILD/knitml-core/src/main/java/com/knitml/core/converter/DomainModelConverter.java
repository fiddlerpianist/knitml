package com.knitml.core.converter;


public interface DomainModelConverter<S> {

	Object convert(S sourceModel);
	
}
