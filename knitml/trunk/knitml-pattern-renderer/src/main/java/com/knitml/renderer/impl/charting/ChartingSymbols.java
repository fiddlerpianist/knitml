package com.knitml.renderer.impl.charting;

import java.util.HashMap;
import java.util.Map;

import com.knitml.core.common.DecreaseType;
import com.knitml.core.common.IncreaseType;

public class ChartingSymbols {

	private static final ChartingSymbols instance = new ChartingSymbols();

	public static ChartingSymbols getInstance() {
		return instance;
	}

	private Map<String,String> symbols = new HashMap<String,String>();

	private ChartingSymbols() {
		symbols.put("knit", "k");
		symbols.put("purl", "p");
		symbols.put(DecreaseType.K2TOG.name(), "/");
		symbols.put(DecreaseType.SSK.name(), "\\");
		symbols.put(IncreaseType.YO.name(), "o");
	}

	public String getSymbol(String key) {
		return symbols.get(key); 
	}
	
	public String getSymbol(Enum<?> enumValue) {
		return getSymbol(enumValue.name());
	}

}
