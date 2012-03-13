package com.knitml.renderer.chart

import java.util.LinkedHashMap
import java.util.Map

import com.knitml.core.model.operations.DiscreteInlineOperation
import com.knitml.core.model.operations.inline.CrossStitches
import com.knitml.core.model.operations.inline.OperationGroup

import static com.knitml.renderer.chart.OperationGroupBuilder.group
import static com.knitml.renderer.chart.ChartElement.*

public class ChartElementFinder {
	
	public static Map<DiscreteInlineOperation, ChartElement> groupToChartElementMap = [:]
	public static Map<Integer, ChartElement> groupSizeToCustomCableMap = [:]
	
	static {
		// 2-st
		groupToChartElementMap[group(2).rc(1,1).k(2).build()] = CBL_1_1_RC 
		groupToChartElementMap[group(2).lc(1,1).k(2).build()] = CBL_1_1_LC 
		groupToChartElementMap[group(2).rc(1,1).k(1).p(1).build()] = CBL_1_1_RPC 
		groupToChartElementMap[group(2).lc(1,1).p(1).k(1).build()] = CBL_1_1_LPC 
		groupToChartElementMap[group(2).rc(1,1).k_tbl(2).build()] = CBL_1_1_RT 
		groupToChartElementMap[group(2).lc(1,1).k_tbl(2).build()] = CBL_1_1_LT 
		groupToChartElementMap[group(2).rc(1,1).k_tbl(1).p(1).build()] = CBL_1_1_RPT 
		groupToChartElementMap[group(2).lc(1,1).p(1).k_tbl(1).build()] = CBL_1_1_LPT
		// 3-st
		groupToChartElementMap[group(3).rc(2,1).k(3).build()] = CBL_2_1_RC
		groupToChartElementMap[group(3).lc(2,1).k(3).build()] = CBL_2_1_LC
		groupToChartElementMap[group(3).rc(2,1).k(2).p(1).build()] = CBL_2_1_RPC
		groupToChartElementMap[group(3).lc(2,1).p(1).k(2).build()] = CBL_2_1_LPC
		groupToChartElementMap[group(3).rc(1,2).k(1).p(2).build()] = CBL_1_2_RPC
		groupToChartElementMap[group(3).lc(1,2).p(2).k(1).build()] = CBL_1_2_LPC
		groupToChartElementMap[group(3).rc(1,1,1).k(3).build()] = CBL_1_1_1_RC
		groupToChartElementMap[group(3).lc(1,1,1).k(3).build()] = CBL_1_1_1_LC
		groupToChartElementMap[group(3).rc(1,1,1).k(3).build()] = CBL_1_1_1_RC
		groupToChartElementMap[group(3).lc(1,1,1).k(3).build()] = CBL_1_1_1_LC
		groupToChartElementMap[group(3).rc(1,1,1).k(1).p(1).k(1).build()] = CBL_1_1_1_RPC
		groupToChartElementMap[group(3).lc(1,1,1).k(1).p(1).k(1).build()] = CBL_1_1_1_LPC
		groupToChartElementMap[group(3).rrc(1,1,1).k(3).build()] = CBL_1_1_1_RRC
		groupToChartElementMap[group(3).lrc(1,1,1).k(3).build()] = CBL_1_1_1_LRC
		groupToChartElementMap[group(3).rc(2,1).k_tbl(2).k_tbl(1).build()] = CBL_2_1_RT
		groupToChartElementMap[group(3).lc(2,1).k_tbl(1).k_tbl(2).build()] = CBL_2_1_LT
		groupToChartElementMap[group(3).rc(2,1).k_tbl(2).p(1).build()] = CBL_2_1_RPT
		groupToChartElementMap[group(3).lc(2,1).p(1).k_tbl(2).build()] = CBL_2_1_LPT
		groupToChartElementMap[group(3).rc(1,1,1).k_tbl(1).p(1).k_tbl(1).build()] = CBL_1_1_1_RT_PURL
		groupToChartElementMap[group(3).lc(1,1,1).k_tbl(1).p(1).k_tbl(1).build()] = CBL_1_1_1_LT_PURL
		// 4-st
		groupToChartElementMap[group(4).rc(2,2).k(4).build()] = CBL_2_2_RC
		groupToChartElementMap[group(4).lc(2,2).k(4).build()] = CBL_2_2_LC
		groupToChartElementMap[group(4).rc(2,2).k(2).p(2).build()] = CBL_2_2_RPC
		groupToChartElementMap[group(4).lc(2,2).k(2).p(2).build()] = CBL_2_2_LPC
		groupToChartElementMap[group(4).rc(1,3).k(4).build()] = CBL_1_3_RC
		groupToChartElementMap[group(4).lc(1,3).k(4).build()] = CBL_1_3_LC
		groupToChartElementMap[group(4).rc(1,3).k(1).p(3).build()] = CBL_1_3_RPC
		groupToChartElementMap[group(4).lc(1,3).p(3).k(1).build()] = CBL_1_3_LPC
		groupToChartElementMap[group(4).rc(3,1).k(4).build()] = CBL_3_1_RC
		groupToChartElementMap[group(4).lc(3,1).k(4).build()] = CBL_3_1_LC
		groupToChartElementMap[group(4).rc(3,1).k(3).p(1).build()] = CBL_3_1_RPC
		groupToChartElementMap[group(4).lc(3,1).p(1).k(3).build()] = CBL_3_1_LPC
		groupToChartElementMap[group(4).rc(1,2,1).k(4).build()] = CBL_1_2_1_RC
		groupToChartElementMap[group(4).lc(1,2,1).k(4).build()] = CBL_1_2_1_LC
		groupToChartElementMap[group(4).rc(1,2,1).k(1).p(2).k(1).build()] = CBL_1_2_1_RPC
		groupToChartElementMap[group(4).lc(1,2,1).k(1).p(2).k(1).build()] = CBL_1_2_1_LPC
		groupToChartElementMap[group(4).rc(2,2).k_tbl(2).k_tbl(2).build()] = CBL_2_2_RT
		groupToChartElementMap[group(4).lc(2,2).k_tbl(2).k_tbl(2).build()] = CBL_2_2_LT
		
		groupSizeToCustomCableMap[2] = CBL_2ST_CUSTOM
		groupSizeToCustomCableMap[3] = CBL_3ST_CUSTOM
		groupSizeToCustomCableMap[4] = CBL_4ST_CUSTOM
	}
	
	public ChartElement findChartElement(OperationGroup group) {
		ChartElement chartElement = groupToChartElementMap[group]
		if (chartElement == null && group.isCableInstruction()) {
			chartElement = groupSizeToCustomCableMap[group.size]
		}
		return chartElement
	}

}
