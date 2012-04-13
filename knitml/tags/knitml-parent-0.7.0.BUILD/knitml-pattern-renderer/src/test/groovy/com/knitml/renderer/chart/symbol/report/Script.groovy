package com.knitml.renderer.chart.symbol.report

import com.knitml.renderer.chart.advisor.impl.StitchMasterySymbolAdvisor
import com.knitml.renderer.chart.advisor.impl.AireRiverSymbolAdvisor

def dumper = new SymbolProviderMappingReport(new StitchMasterySymbolAdvisor())
//def dumper = new SymbolProviderMappingReport(new AireRiverSymbolAdvisor())
def writer = new FileWriter("target/dump.html")
dumper.writeMap(writer)
writer.close()
