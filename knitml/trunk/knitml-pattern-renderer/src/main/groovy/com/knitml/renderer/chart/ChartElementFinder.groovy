package com.knitml.renderer.chart

import static com.knitml.renderer.chart.OperationBuilder.slip
import static com.knitml.renderer.chart.OperationBuilder.slip_kw
import static com.knitml.renderer.chart.ChartElement.*
import static com.knitml.renderer.chart.OperationBuilder.group
import static com.knitml.renderer.chart.OperationBuilder.knit
import static com.knitml.renderer.chart.OperationBuilder.purl
import static com.knitml.renderer.chart.OperationBuilder.decrease
import static com.knitml.renderer.chart.OperationBuilder.increase

import java.util.Map

import com.google.common.collect.BiMap
import com.google.common.collect.EnumHashBiMap
import com.knitml.core.common.DecreaseType
import com.knitml.core.common.IncreaseType
import com.knitml.core.common.LoopToWork
import com.knitml.core.common.SlipDirection
import com.knitml.core.common.Wise
import com.knitml.core.common.YarnPosition
import com.knitml.core.model.operations.DiscreteInlineOperation
import com.knitml.core.model.operations.inline.Decrease
import com.knitml.core.model.operations.inline.DoubleDecrease
import com.knitml.core.model.operations.inline.Increase
import com.knitml.core.model.operations.inline.IncreaseIntoNextStitch
import com.knitml.core.model.operations.inline.Knit
import com.knitml.core.model.operations.inline.NoStitch
import com.knitml.core.model.operations.inline.OperationGroup
import com.knitml.core.model.operations.inline.Purl
import com.knitml.core.model.operations.inline.Slip

public class ChartElementFinder {

	private static BiMap<ChartElement, DiscreteInlineOperation> chartElementToOperationMap = EnumHashBiMap.create(ChartElement)
	private static Map<Integer, ChartElement> groupSizeToCustomCableMap = [:]

	static {
		// knits / purls / slips
		chartElementToOperationMap[K] = knit()
		chartElementToOperationMap[K_TW] = knit LoopToWork.TRAILING
		chartElementToOperationMap[P] = purl()
		chartElementToOperationMap[P_TW] = purl LoopToWork.TRAILING
		chartElementToOperationMap[SL] = slip()
		chartElementToOperationMap[SL_WYIF] = slip YarnPosition.FRONT
		chartElementToOperationMap[SL_KW] = slip_kw()
		chartElementToOperationMap[SL_KW_WYIF] = slip_kw YarnPosition.FRONT
		// decreases
		chartElementToOperationMap[DECREASE] = decrease()
		chartElementToOperationMap[K2TOG] = decrease DecreaseType.K2TOG
		chartElementToOperationMap[K2TOG_TBL] = decrease DecreaseType.K2TOG_TBL
		chartElementToOperationMap[SSK] = decrease DecreaseType.SSK
		chartElementToOperationMap[SKP] = decrease DecreaseType.SKP
		chartElementToOperationMap[K3TOG] = decrease DecreaseType.K3TOG
		chartElementToOperationMap[SSSK] = decrease DecreaseType.SSSK
		chartElementToOperationMap[SK2P] = decrease DecreaseType.SK2P
		chartElementToOperationMap[CDD] = decrease DecreaseType.CDD
		chartElementToOperationMap[P2TOG] = decrease DecreaseType.P2TOG
		chartElementToOperationMap[P2TOG_TBL] = decrease DecreaseType.P2TOG_TBL
		chartElementToOperationMap[SSP] = decrease DecreaseType.SSP
		chartElementToOperationMap[P3TOG] = decrease DecreaseType.P3TOG
		// 1-st increases
		chartElementToOperationMap[M1] = increase IncreaseType.M1 
		chartElementToOperationMap[M1R] = increase IncreaseType.M1R 
		chartElementToOperationMap[M1L] = increase IncreaseType.M1L 
		chartElementToOperationMap[KFB] = increase IncreaseType.KFB 
		chartElementToOperationMap[M1P] = increase IncreaseType.M1P 
		chartElementToOperationMap[M1RP] = increase IncreaseType.M1RP 
		chartElementToOperationMap[M1LP] = increase IncreaseType.M1LP 
		chartElementToOperationMap[PFB] = increase IncreaseType.PFB
		// TODO
		//		// knit decreases
		//		 S2K2P2(
		//				1), K4TOG(1), K5TOG(1), K6TOG(1), K7TOG(1), K8TOG(1), K9TOG(1), DEC4_TO_1(
		//				1), DEC5_TO_1(1), DEC6_TO_1(1), DEC7_TO_1(1), DEC8_TO_1(1), DEC9_TO_1(
		//				1),
		//		// purl decreases
		//		P2TOG_SWYIF_PSSO(1), SL2_P1_P2SSO(
		//				1), CDDP(1), SWYIF_P2TOGTBL_PSSO(1),
		// knit increases
		chartElementToOperationMap[KFBF] = new IncreaseIntoNextStitch(null, group(1).k(1).k_tbl(1).k(1).build().operations)
		chartElementToOperationMap[KPK_NEXT_ST] = new IncreaseIntoNextStitch(null, group(1).k(1).p(1).k(1).build().operations)
		chartElementToOperationMap[INC1_TO_3] = group 1 m1 2 build()
		chartElementToOperationMap[INC1_TO_4] = group 1 m1 3 build()
		chartElementToOperationMap[INC1_TO_5] = group 1 m1 4 build()
		chartElementToOperationMap[INC1_TO_6] = group 1 m1 5 build()
		chartElementToOperationMap[INC1_TO_7] = group 1 m1 6 build()
		chartElementToOperationMap[INC1_TO_8] = group 1 m1 7 build()
		chartElementToOperationMap[INC1_TO_9] = group 1 m1 8 build()
		// purl increases
		chartElementToOperationMap[PFBF] = new IncreaseIntoNextStitch(null, group(1).p(1).p_tbl(1).p(1).build().operations)
		chartElementToOperationMap[PKP_NEXT_ST] = new IncreaseIntoNextStitch(null, group(1).p(1).k(1).p(1).build().operations)
		// yarn-over increases
		chartElementToOperationMap[YO] = increase IncreaseType.YO
		chartElementToOperationMap[YO2] = group 1 yo 2 build()
		chartElementToOperationMap[YO3] = group 1 yo 3 build()
		chartElementToOperationMap[YO4] = group 1 yo 4 build()
		chartElementToOperationMap[YO5] = group 1 yo 5 build()
		chartElementToOperationMap[YO6] = group 1 yo 6 build()
		chartElementToOperationMap[YO7] = group 1 yo 7 build()
		chartElementToOperationMap[YO8] = group 1 yo 8 build()
		chartElementToOperationMap[YO9] = group 1 yo 9 build()
		chartElementToOperationMap[KYK_NEXT_ST] = new IncreaseIntoNextStitch(null, group(1).k(1).yo(1).k(1).build().operations)
		chartElementToOperationMap[PYP_NEXT_ST] = new IncreaseIntoNextStitch(null, group(1).p(1).yo(1).p(1).build().operations)
		

		chartElementToOperationMap[NS] = new NoStitch()

		// 2-st
		chartElementToOperationMap[CBL_1_1_RC] = group 2 rc 1,1 k 2 build()
		chartElementToOperationMap[CBL_1_1_LC] = group 2 lc 1,1 k 2 build()
		chartElementToOperationMap[CBL_1_1_RPC] = group 2 rc 1,1 k 1 p 1 build()
		chartElementToOperationMap[CBL_1_1_LPC] = group 2 lc 1,1 p 1 k 1 build()
		chartElementToOperationMap[CBL_1_1_RT] = group 2 rc 1,1 k_tbl 2 build()
		chartElementToOperationMap[CBL_1_1_LT] = group 2 lc 1,1 k_tbl 2 build()
		chartElementToOperationMap[CBL_1_1_RPT] = group 2 rc 1,1 k_tbl 1 p 1 build()
		chartElementToOperationMap[CBL_1_1_LPT] = group 2 lc 1,1 p 1 k_tbl 1 build()
		// 3-st
		chartElementToOperationMap[CBL_2_1_RC] = group 3 rc 2,1 k 3 build()
		chartElementToOperationMap[CBL_2_1_LC] = group 3 lc 2,1 k 3 build()
		chartElementToOperationMap[CBL_2_1_RPC] = group 3 rc 2,1 k 2 p 1 build()
		chartElementToOperationMap[CBL_2_1_LPC] = group 3 lc 2,1 p 1 k 2 build()
		chartElementToOperationMap[CBL_1_2_RPC] = group 3 rc 1,2 k 1 p 2 build()
		chartElementToOperationMap[CBL_1_2_LPC] = group 3 lc 1,2 p 2 k 1 build()
		chartElementToOperationMap[CBL_1_1_1_RC] = group 3 rc 1,1,1 k 3 build()
		chartElementToOperationMap[CBL_1_1_1_LC] = group 3 lc 1,1,1 k 3 build()
		chartElementToOperationMap[CBL_1_1_1_RC] = group 3 rc 1,1,1 k 3 build()
		chartElementToOperationMap[CBL_1_1_1_LC] = group 3 lc 1,1,1 k 3 build()
		chartElementToOperationMap[CBL_1_1_1_RPC] = group 3 rc 1,1,1 k 1 p 1 k 1 build()
		chartElementToOperationMap[CBL_1_1_1_LPC] = group 3 lc 1,1,1 k 1 p 1 k 1 build()
		chartElementToOperationMap[CBL_1_1_1_RRC] = group 3 rrc 1,1,1 k 3 build()
		chartElementToOperationMap[CBL_1_1_1_LRC] = group 3 lrc 1,1,1 k 3 build()
		chartElementToOperationMap[CBL_2_1_RT] = group 3 rc 2,1 k_tbl 2 k_tbl 1 build()
		chartElementToOperationMap[CBL_2_1_LT] = group 3 lc 2,1 k_tbl 1 k_tbl 2 build()
		chartElementToOperationMap[CBL_2_1_RPT] = group 3 rc 2,1 k_tbl 2 p 1 build()
		chartElementToOperationMap[CBL_2_1_LPT] = group 3 lc 2,1 p 1 k_tbl 2 build()
		chartElementToOperationMap[CBL_1_1_1_RT_PURL] = group 3 rc 1,1,1 k_tbl 1 p 1 k_tbl 1 build()
		chartElementToOperationMap[CBL_1_1_1_LT_PURL] = group 3 lc 1,1,1 k_tbl 1 p 1 k_tbl 1 build()
		// 4-st
		chartElementToOperationMap[CBL_2_2_RC] = group 4 rc 2,2 k 4 build()
		chartElementToOperationMap[CBL_2_2_LC] = group 4 lc 2,2 k 4 build()
		chartElementToOperationMap[CBL_2_2_RPC] = group 4 rc 2,2 k 2 p 2 build()
		chartElementToOperationMap[CBL_2_2_LPC] = group 4 lc 2,2 k 2 p 2 build()
		chartElementToOperationMap[CBL_1_3_RC] = group 4 rc 1,3 k 4 build()
		chartElementToOperationMap[CBL_1_3_LC] = group 4 lc 1,3 k 4 build()
		chartElementToOperationMap[CBL_1_3_RPC] = group 4 rc 1,3 k 1 p 3 build()
		chartElementToOperationMap[CBL_1_3_LPC] = group 4 lc 1,3 p 3 k 1 build()
		chartElementToOperationMap[CBL_3_1_RC] = group 4 rc 3,1 k 4 build()
		chartElementToOperationMap[CBL_3_1_LC] = group 4 lc 3,1 k 4 build()
		chartElementToOperationMap[CBL_3_1_RPC] = group 4 rc 3,1 k 3 p 1 build()
		chartElementToOperationMap[CBL_3_1_LPC] = group 4 lc 3,1 p 1 k 3 build()
		chartElementToOperationMap[CBL_1_2_1_RC] = group 4 rc 1,2,1 k 4 build()
		chartElementToOperationMap[CBL_1_2_1_LC] = group 4 lc 1,2,1 k 4 build()
		chartElementToOperationMap[CBL_1_2_1_RPC] = group 4 rc 1,2,1 k 1 p 2 k 1 build()
		chartElementToOperationMap[CBL_1_2_1_LPC] = group 4 lc 1,2,1 k 1 p 2 k 1 build()
		chartElementToOperationMap[CBL_2_2_RT] = group 4 rc 2,2 k_tbl 2 k_tbl 2 build()
		chartElementToOperationMap[CBL_2_2_LT] = group 4 lc 2,2 k_tbl 2 k_tbl 2 build()

		groupSizeToCustomCableMap[2] = CBL_2ST_CUSTOM
		groupSizeToCustomCableMap[3] = CBL_3ST_CUSTOM
		groupSizeToCustomCableMap[4] = CBL_4ST_CUSTOM
	}

	ChartElement findChartElementBy(DiscreteInlineOperation operation) {
		if (operation instanceof OperationGroup) {
			return findChartElementByOperationGroup(operation)
		}
		// not an operation group
		return chartElementToOperationMap.inverse()[operation]
	}

	DiscreteInlineOperation findOperationBy(ChartElement element) {
		return chartElementToOperationMap[element]
	}

	protected ChartElement findChartElementByOperationGroup(OperationGroup group) {
		def bimap = chartElementToOperationMap.inverse()
		ChartElement chartElement = bimap.get(group)
		if (chartElement == null && group.isCableInstruction()) {
			chartElement = groupSizeToCustomCableMap[group.size]
		}
		return chartElement
	}
}
