package com.knitml.renderer.chart.symbol;

public class Symbol {
	
	private String symbolSetId;
	private String symbol;

	public Symbol(String symbolSetId, String symbol) {
		this.symbolSetId = symbolSetId;
		this.symbol = symbol;
	}
	
	public String getSymbolSetId() {
		return symbolSetId;
	}
	public String getSymbol() {
		return symbol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result
				+ ((symbolSetId == null) ? 0 : symbolSetId.hashCode());
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
		Symbol other = (Symbol) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (symbolSetId == null) {
			if (other.symbolSetId != null)
				return false;
		} else if (!symbolSetId.equals(other.symbolSetId))
			return false;
		return true;
	}

}
