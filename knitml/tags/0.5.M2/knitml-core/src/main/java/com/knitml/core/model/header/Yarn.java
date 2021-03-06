package com.knitml.core.model.header;

import javax.measure.Measurable;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;

import com.knitml.core.common.ValidationException;


/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class Yarn {
	
	protected String id;
	protected String symbol;
	protected String messageKey;
	protected String label;

	protected Measurable<Length> totalLength;
	protected Measurable<Mass> totalWeight;
	protected Color color;
	protected YarnType yarnType;
	
	private String symbolOverride;
	
	public Yarn() {
	}
	
	public Yarn (String id, String symbol) {
		this.id = id;
		this.symbol = symbol;
		this.messageKey = null;
	}
	
	@Override
	public String toString() {
		return getSymbol() == null ? super.toString() : getSymbol();
	}
	
	public String getLabel() {
		return label;
	}

	public YarnType getYarnType() {
		return yarnType;
	}

	public Color getColor() {
		return color;
	}

	public String getSymbol() {
		if (symbolOverride != null) {
			return symbolOverride;
		} else if (symbol != null) {
			return symbol;
		} else {
			return null;
		}
	}
	
	public void setSymbol(String symbol) {
		this.symbolOverride = symbol;
	}

	public String getId() {
		return id;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public Measurable<Length> getTotalLength() {
		return totalLength;
	}

	public Measurable<Mass> getTotalWeight() {
		return totalWeight;
	}
	
	public void afterPropertiesSet() {
		if (yarnType == null) {
			throw new ValidationException("No yarn type was able to be found for this yarn", this);
		}
	}
	
}
