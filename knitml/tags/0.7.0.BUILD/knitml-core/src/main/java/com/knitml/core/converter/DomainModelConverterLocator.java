package com.knitml.core.converter;

public interface DomainModelConverterLocator<S> {

	DomainModelConverter<S> locateConverter(S sourceModel);

}
