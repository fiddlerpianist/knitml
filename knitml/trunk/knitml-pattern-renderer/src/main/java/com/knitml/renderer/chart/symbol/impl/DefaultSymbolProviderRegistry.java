package com.knitml.renderer.chart.symbol.impl;

import java.util.List;

import com.knitml.renderer.chart.symbol.SymbolProvider;
import com.knitml.renderer.chart.symbol.SymbolProviderRegistry;

public class DefaultSymbolProviderRegistry implements SymbolProviderRegistry {
	
	private List<SymbolProvider> symbolProviders;
	
	public DefaultSymbolProviderRegistry(List<SymbolProvider> symbolProviders) {
		this.symbolProviders = symbolProviders;
	}
	
	public SymbolProvider getSymbolProvider(String id) {
		return symbolProviders.get(0);
	}

}
