package com.knitml.engine.common;

import java.util.Map;

public class KnittingEngineException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer totalRowNumber;
	private Integer localRowNumber;
	private Map<String, Integer> instructionRepeatCounts;
	private String additionalInformation;

	public void setAdditionalInformation(String path) {
		this.additionalInformation = path;
	}

	public void setTotalRowNumber(Integer offendingRowNumber) {
		this.totalRowNumber = offendingRowNumber;
	}

	public void setLocalRowNumber(Integer offendingRowNumber) {
		this.localRowNumber = offendingRowNumber;
	}

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

	@Override
	public String getMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.getMessage());
		if (instructionRepeatCounts != null) {
			for (String id : instructionRepeatCounts.keySet()) {
				buffer.append("\nAt repeat ").append(instructionRepeatCounts.get(id))
						.append(" for instruction ID [").append(id).append("]");
			}
		}
		if (totalRowNumber != null) {
			buffer.append("\nAt execution of row "
					+ totalRowNumber
					+ (localRowNumber != null ? " (local row " + localRowNumber
							+ ")" : "") + ": ");
		}
		if (additionalInformation != null) {
			buffer.append("\nAdditional information: ").append(this.additionalInformation);
		}
		return buffer.toString();
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setInstructionRepeatCounts(
			Map<String, Integer> instructionRepeatCounts) {
		this.instructionRepeatCounts = instructionRepeatCounts;
	}

}
