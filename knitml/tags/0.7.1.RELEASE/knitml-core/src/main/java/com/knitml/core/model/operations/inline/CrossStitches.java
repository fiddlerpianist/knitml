package com.knitml.core.model.operations.inline;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.CrossType;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.operations.DiscreteInlineOperation;


public class CrossStitches implements DiscreteInlineOperation {
	
	protected Integer first;
	protected Integer next;
	protected CrossType type;
	protected Integer skip;
	protected CrossType skipType;
	
	public CrossStitches() {
	}
	
	public CrossStitches(Integer first, CrossType type, Integer next) {
		super();
		this.first = first;
		this.type = type;
		this.next = next;
	}
	
	public CrossStitches(Integer first, Integer skip, Integer next, CrossType type, CrossType skipType) {
		super();
		this.first = first;
		this.type = type;
		this.next = next;
		this.skip = skip;
		this.skipType = skipType;
	}
	
	public List<CrossStitches> canonicalize() {
		List<CrossStitches> newOps = new ArrayList<CrossStitches>(1);
		newOps.add(this);
		return newOps;
	}

	public Integer getFirst() {
		return first;
	}
	public Integer getNext() {
		return next;
	}
	public CrossType getType() {
		return type;
	}
	public int getAdvanceCount() {
		return 0;
	}
	public int getIncreaseCount() {
		return 0;
	}
	public void setFirst(Integer first) {
		this.first = first;
	}
	public void setNext(Integer next) {
		this.next = next;
	}
	public void setType(CrossType type) {
		this.type = type;
	}

	public Integer getSkip() {
		return skip;
	}

	public void setSkip(Integer skip) {
		this.skip = skip;
	}

	public CrossType getSkipType() {
		return skipType;
	}

	public void setSkipType(CrossType skipType) {
		this.skipType = skipType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		result = prime * result + ((skip == null) ? 0 : skip.hashCode());
		result = prime * result
				+ ((skipType == null) ? 0 : skipType.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrossStitches other = (CrossStitches) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (next == null) {
			if (other.next != null)
				return false;
		} else if (!next.equals(other.next))
			return false;
		if (skip == null) {
			if (other.skip != null)
				return false;
		} else if (!skip.equals(other.skip))
			return false;
		if (skipType != other.skipType)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public void validate() {
		if (skip != null && skipType == null) {
			throw new ValidationException(
					"If 'skip' is specified, then 'skipType' must also be specified");
		} else
		if (skip == null && skipType != null) {
			throw new ValidationException(
					"If 'skipType' is specified, then 'skip' must also be specified");
		}
	}
	
}
