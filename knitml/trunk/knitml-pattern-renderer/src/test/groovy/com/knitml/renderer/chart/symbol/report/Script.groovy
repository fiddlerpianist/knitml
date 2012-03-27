package com.knitml.renderer.chart.symbol.report

import com.knitml.renderer.chart.advisor.impl.AireRiverSymbolAdvisor
import com.knitml.renderer.chart.advisor.impl.StitchMasterySymbolAdvisor;

def dumper = new SymbolProviderMappingReport(new StitchMasterySymbolAdvisor())
def writer = new StringWriter(2048)
dumper.writeMap(writer)
println writer