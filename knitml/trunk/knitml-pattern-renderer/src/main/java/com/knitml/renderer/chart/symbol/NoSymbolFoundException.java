package com.knitml.renderer.chart.symbol;

import com.knitml.renderer.chart.ChartElement;
import com.knitml.renderer.common.RenderingException;

public class NoSymbolFoundException extends RenderingException {

	private static final long serialVersionUID = 1L;

	public NoSymbolFoundException(SymbolProvider provider, ChartElement chartElement) {
		super("Symbol Provider: " + provider.getClass().getName() + ", Chart Element: " + chartElement.toString());
	}

}
