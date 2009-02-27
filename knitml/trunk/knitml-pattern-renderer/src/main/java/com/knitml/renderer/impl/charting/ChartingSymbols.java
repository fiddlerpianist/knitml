package com.knitml.renderer.impl.charting;

import static com.knitml.renderer.impl.charting.ChartElement.K2TOG;
import static com.knitml.renderer.impl.charting.ChartElement.SSK;
import static com.knitml.renderer.impl.charting.ChartElement.YO;
import static com.knitml.renderer.impl.charting.ChartElement.K;
import static com.knitml.renderer.impl.charting.ChartElement.P;

import java.util.HashMap;
import java.util.Map;

public class ChartingSymbols {

	private static final ChartingSymbols instance = new ChartingSymbols();

	public static ChartingSymbols getInstance() {
		return instance;
	}

	private Map<ChartElement,String> symbols = new HashMap<ChartElement,String>();

	private ChartingSymbols() {
		symbols.put(K, "k");
		symbols.put(P, "p");
		symbols.put(K2TOG, "/");
		symbols.put(SSK, "\\");
		symbols.put(YO, "o");
	}

	public String getSymbol(String key) {
		return symbols.get(key); 
	}
	
	public String getSymbol(Enum<?> enumValue) {
		return getSymbol(enumValue.name());
	}

}
