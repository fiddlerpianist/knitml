package com.knitml.core.xml.translator;

import org.jibx.extras.IdRefMapperBase;

import com.knitml.core.model.common.Identifiable;

public class IdentifiableTranslator extends IdRefMapperBase {

	public IdentifiableTranslator(String uri, int index, String name) {
		super(uri, index, name);
	}

	@Override
	protected String getIdValue(Object element) {
		return ((Identifiable)element).getId();
	}

}