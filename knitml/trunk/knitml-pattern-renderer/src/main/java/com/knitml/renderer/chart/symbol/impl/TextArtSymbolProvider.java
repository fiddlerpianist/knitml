package com.knitml.renderer.chart.symbol.impl;

import static com.knitml.renderer.chart.ChartElement.CDD;
import static com.knitml.renderer.chart.ChartElement.DECREASE;
import static com.knitml.renderer.chart.ChartElement.K;
import static com.knitml.renderer.chart.ChartElement.K2TOG;
import static com.knitml.renderer.chart.ChartElement.K2TOG_TBL;
import static com.knitml.renderer.chart.ChartElement.K3TOG;
import static com.knitml.renderer.chart.ChartElement.K_TW;
import static com.knitml.renderer.chart.ChartElement.M1;
import static com.knitml.renderer.chart.ChartElement.NS;
import static com.knitml.renderer.chart.ChartElement.P;
import static com.knitml.renderer.chart.ChartElement.P2TOG;
import static com.knitml.renderer.chart.ChartElement.P2TOG_TBL;
import static com.knitml.renderer.chart.ChartElement.P3TOG;
import static com.knitml.renderer.chart.ChartElement.P_TW;
import static com.knitml.renderer.chart.ChartElement.SK2P;
import static com.knitml.renderer.chart.ChartElement.SKP;
import static com.knitml.renderer.chart.ChartElement.SL;
import static com.knitml.renderer.chart.ChartElement.SSK;
import static com.knitml.renderer.chart.ChartElement.SSP;
import static com.knitml.renderer.chart.ChartElement.SSSK;
import static com.knitml.renderer.chart.ChartElement.YO;

public class TextArtSymbolProvider extends AbstractSymbolProvider {

	protected void initializeSymbols() {
		symbols.put(K, "-");
		symbols.put(P, "=");
		symbols.put(SL, "S");
		symbols.put(K_TW, "\u0086");
		symbols.put(P_TW, "\u01C2");
		symbols.put(YO, "o");
		symbols.put(M1, "M");
		symbols.put(SSK, "\\");
		symbols.put(K2TOG, "/");
		symbols.put(SSP, "p");
		symbols.put(P2TOG, "r");
		symbols.put(SKP, "L");
		symbols.put(K2TOG_TBL, "\u0166");
		symbols.put(P2TOG_TBL, "\u0167");
		symbols.put(SSSK, "\u015E");
		symbols.put(K3TOG, "\u019B");
		symbols.put(P3TOG, "\u019C");
		symbols.put(CDD, "\u0297");
		symbols.put(SK2P, "\u0286");
		symbols.put(DECREASE, "1");
		symbols.put(NS, "x");
	}

}
