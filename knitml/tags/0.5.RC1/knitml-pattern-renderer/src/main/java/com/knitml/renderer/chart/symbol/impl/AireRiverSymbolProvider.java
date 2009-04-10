package com.knitml.renderer.chart.symbol.impl;

import static com.knitml.renderer.chart.ChartElement.*;
import static com.knitml.renderer.chart.ChartElement.K2TOG;
import static com.knitml.renderer.chart.ChartElement.NS;
import static com.knitml.renderer.chart.ChartElement.P;
import static com.knitml.renderer.chart.ChartElement.SL;
import static com.knitml.renderer.chart.ChartElement.SSK;
import static com.knitml.renderer.chart.ChartElement.YO;

public class AireRiverSymbolProvider extends
		AbstractSymbolProvider {

	@Override
	protected void initializeSymbols() {
		symbols.put(K, "-"); // note that this is a blank space, not the | symbol
		symbols.put(P, "h");
		symbols.put(SL, ",");
		symbols.put(K_TW, "\u00f2");
		symbols.put(P_TW, "\u00f3");
		symbols.put(YO, "j");
		symbols.put(M1, "k");
		symbols.put(SSK, "\\");
		symbols.put(K2TOG, "|");
		symbols.put(SSP, "c");
		symbols.put(P2TOG, "e");
		symbols.put(SKP, "s");
		symbols.put(K2TOG_TBL, "x");
		symbols.put(P2TOG_TBL, "w");
		symbols.put(SSSK, "a");
		symbols.put(K3TOG, "f");
		symbols.put(P3TOG, "r");
		symbols.put(CDD, "v");
		symbols.put(SK2P, "a");
		symbols.put(DECREASE, "1");
		symbols.put(NS, "z");
	}

}
