package com.knitml.engine.common;

import java.util.Collections;
import java.util.List;

import com.knitml.core.model.directions.DiscreteInlineOperation;
import com.knitml.core.model.directions.Operation;

public abstract class KnittingEngineException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Object> locationBreadcrumb;
	private Operation offendingOperation;
	private String additionalInformation;

	public KnittingEngineException() {
		super();
	}

	public KnittingEngineException(String message, Throwable cause) {
		super(message, cause);
	}

	public KnittingEngineException(String message) {
		super(message);
	}

	public KnittingEngineException(Throwable cause) {
		super(cause);
	}

	public void setAdditionalInformation(String path) {
		this.additionalInformation = path;
	}

	public List<Object> getLocationBreadcrumb() {
		return (locationBreadcrumb == null ? null : Collections
				.unmodifiableList(locationBreadcrumb));
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setLocationBreadcrumb(List<Object> locationBreadcrumb) {
		this.locationBreadcrumb = locationBreadcrumb;
	}

	public Operation getOffendingOperation() {
		return offendingOperation;
	}

	public void setOffendingOperation(Operation offendingOperation) {
		this.offendingOperation = offendingOperation;
	}

}
