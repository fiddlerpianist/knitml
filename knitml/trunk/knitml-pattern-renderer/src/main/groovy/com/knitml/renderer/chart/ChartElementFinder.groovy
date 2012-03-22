package com.knitml.renderer.chart

import static com.knitml.renderer.chart.ChartElement.*
import static com.knitml.renderer.chart.OperationGroupBuilder.group

import java.util.Map

import com.google.common.collect.BiMap
import com.google.common.collect.EnumHashBiMap
import com.knitml.core.common.DecreaseType
import com.knitml.core.common.IncreaseType
import com.knitml.core.common.LoopToWork
import com.knitml.core.common.YarnPosition
import com.knitml.core.model.operations.DiscreteInlineOperation
import com.knitml.core.model.operations.inline.Decrease
import com.knitml.core.model.operations.inline.DoubleDecrease
import com.knitml.core.model.operations.inline.Increase
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
		chartElementToOperationMap[K] = new Knit(null, LoopToWork.LEADING, null)
		chartElementToOperationMap[K_TW] = new Knit(null, LoopToWork.TRAILING, null)
		chartElementToOperationMap[P] = new Purl(null, LoopToWork.LEADING, null)
		chartElementToOperationMap[P_TW] = new Purl(null, LoopToWork.TRAILING, null)
		chartElementToOperationMap[SL] = new Slip(null, null, YarnPosition.BACK, null)
		chartElementToOperationMap[SL_WYIF] = new Slip(null, null, YarnPosition.FRONT, null)
		// decreases
		chartElementToOperationMap[DECREASE] = new Decrease()
		chartElementToOperationMap[K2TOG] = new Decrease(null, DecreaseType.K2TOG, null)
		chartElementToOperationMap[K2TOG_TBL] = new Decrease(null, DecreaseType.K2TOG_TBL, null)
		chartElementToOperationMap[SSK] = new Decrease(null, DecreaseType.SSK, null)
		chartElementToOperationMap[SKP] = new Decrease(null, DecreaseType.SKP, null)
		chartElementToOperationMap[K3TOG] = new DoubleDecrease(null, DecreaseType.K3TOG, null)
		chartElementToOperationMap[SSSK] = new DoubleDecrease(null, DecreaseType.SSSK, null)
		chartElementToOperationMap[SK2P] = new DoubleDecrease(null, DecreaseType.SK2P, null)
		chartElementToOperationMap[CDD] = new DoubleDecrease(null, DecreaseType.CDD, null)
		chartElementToOperationMap[P2TOG] = new Decrease(null, DecreaseType.P2TOG, null)
		chartElementToOperationMap[P2TOG_TBL] = new Decrease(null, DecreaseType.P2TOG_TBL, null)
		chartElementToOperationMap[SSP] = new Decrease(null, DecreaseType.SSP, null)
		chartElementToOperationMap[P3TOG] = new DoubleDecrease(null, DecreaseType.P3TOG, null)
		// 1-st increases
		chartElementToOperationMap[M1] = new Increase(null, IncreaseType.M1, null)
		chartElementToOperationMap[M1R] = new Increase(null, IncreaseType.M1R, null)
		chartElementToOperationMap[M1L] = new Increase(null, IncreaseType.M1L, null)
		chartElementToOperationMap[KFB] = new Increase(null, IncreaseType.KFB, null)
		chartElementToOperationMap[M1P] = new Increase(null, IncreaseType.M1P, null)
		chartElementToOperationMap[M1RP] = new Increase(null, IncreaseType.M1RP, null)
		chartElementToOperationMap[M1LP] = new Increase(null, IncreaseType.M1LP, null)
		chartElementToOperationMap[PFB] = new Increase(null, IncreaseType.PFB, null)
		chartElementToOperationMap[YO] = new Increase(null, IncreaseType.YO, null)
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
		//		KFBF(1), KPK_NEXT_ST(1), INC1_TO_3(1), INC1_TO_4(
		//				1), INC1_TO_5(1), INC1_TO_6(1), INC1_TO_7(1), INC1_TO_8(1), INC1_TO_9(
		//				1),
		//		// purl increases
		//		PFBF(1), PKP_NEXT_ST(1),
		//		// yarn-over increases
		//		YO2(1), YO3(1), YO4(1), YO5(1), YO6(1), YO7(1), YO8(1), YO9(1), KYK_NEXT_ST(
		//				1), PYP_NEXT_ST(1),


		chartElementToOperationMap[NS] = new NoStitch()

		// 2-st
		chartElementToOperationMap[CBL_1_1_RC] = group(2).rc(1,1).k(2).build()
		chartElementToOperationMap[CBL_1_1_LC] = group(2).lc(1,1).k(2).build()
		chartElementToOperationMap[CBL_1_1_RPC] = group(2).rc(1,1).k(1).p(1).build()
		chartElementToOperationMap[CBL_1_1_LPC] = group(2).lc(1,1).p(1).k(1).build()
		chartElementToOperationMap[CBL_1_1_RT] = group(2).rc(1,1).k_tbl(2).build()
		chartElementToOperationMap[CBL_1_1_LT] = group(2).lc(1,1).k_tbl(2).build()
		chartElementToOperationMap[CBL_1_1_RPT] = group(2).rc(1,1).k_tbl(1).p(1).build()
		chartElementToOperationMap[CBL_1_1_LPT] = group(2).lc(1,1).p(1).k_tbl(1).build()
		// 3-st
		chartElementToOperationMap[CBL_2_1_RC] = group(3).rc(2,1).k(3).build()
		chartElementToOperationMap[CBL_2_1_LC] = group(3).lc(2,1).k(3).build()
		chartElementToOperationMap[CBL_2_1_RPC] = group(3).rc(2,1).k(2).p(1).build()
		chartElementToOperationMap[CBL_2_1_LPC] = group(3).lc(2,1).p(1).k(2).build()
		chartElementToOperationMap[CBL_1_2_RPC] = group(3).rc(1,2).k(1).p(2).build()
		chartElementToOperationMap[CBL_1_2_LPC] = group(3).lc(1,2).p(2).k(1).build()
		chartElementToOperationMap[CBL_1_1_1_RC] = group(3).rc(1,1,1).k(3).build()
		chartElementToOperationMap[CBL_1_1_1_LC] = group(3).lc(1,1,1).k(3).build()
		chartElementToOperationMap[CBL_1_1_1_RC] = group(3).rc(1,1,1).k(3).build()
		chartElementToOperationMap[CBL_1_1_1_LC] = group(3).lc(1,1,1).k(3).build()
		chartElementToOperationMap[CBL_1_1_1_RPC] = group(3).rc(1,1,1).k(1).p(1).k(1).build()
		chartElementToOperationMap[CBL_1_1_1_LPC] = group(3).lc(1,1,1).k(1).p(1).k(1).build()
		chartElementToOperationMap[CBL_1_1_1_RRC] = group(3).rrc(1,1,1).k(3).build()
		chartElementToOperationMap[CBL_1_1_1_LRC] = group(3).lrc(1,1,1).k(3).build()
		chartElementToOperationMap[CBL_2_1_RT] = group(3).rc(2,1).k_tbl(2).k_tbl(1).build()
		chartElementToOperationMap[CBL_2_1_LT] = group(3).lc(2,1).k_tbl(1).k_tbl(2).build()
		chartElementToOperationMap[CBL_2_1_RPT] = group(3).rc(2,1).k_tbl(2).p(1).build()
		chartElementToOperationMap[CBL_2_1_LPT] = group(3).lc(2,1).p(1).k_tbl(2).build()
		chartElementToOperationMap[CBL_1_1_1_RT_PURL] = group(3).rc(1,1,1).k_tbl(1).p(1).k_tbl(1).build()
		chartElementToOperationMap[CBL_1_1_1_LT_PURL] = group(3).lc(1,1,1).k_tbl(1).p(1).k_tbl(1).build()
		// 4-st
		chartElementToOperationMap[CBL_2_2_RC] = group(4).rc(2,2).k(4).build()
		chartElementToOperationMap[CBL_2_2_LC] = group(4).lc(2,2).k(4).build()
		chartElementToOperationMap[CBL_2_2_RPC] = group(4).rc(2,2).k(2).p(2).build()
		chartElementToOperationMap[CBL_2_2_LPC] = group(4).lc(2,2).k(2).p(2).build()
		chartElementToOperationMap[CBL_1_3_RC] = group(4).rc(1,3).k(4).build()
		chartElementToOperationMap[CBL_1_3_LC] = group(4).lc(1,3).k(4).build()
		chartElementToOperationMap[CBL_1_3_RPC] = group(4).rc(1,3).k(1).p(3).build()
		chartElementToOperationMap[CBL_1_3_LPC] = group(4).lc(1,3).p(3).k(1).build()
		chartElementToOperationMap[CBL_3_1_RC] = group(4).rc(3,1).k(4).build()
		chartElementToOperationMap[CBL_3_1_LC] = group(4).lc(3,1).k(4).build()
		chartElementToOperationMap[CBL_3_1_RPC] = group(4).rc(3,1).k(3).p(1).build()
		chartElementToOperationMap[CBL_3_1_LPC] = group(4).lc(3,1).p(1).k(3).build()
		chartElementToOperationMap[CBL_1_2_1_RC] = group(4).rc(1,2,1).k(4).build()
		chartElementToOperationMap[CBL_1_2_1_LC] = group(4).lc(1,2,1).k(4).build()
		chartElementToOperationMap[CBL_1_2_1_RPC] = group(4).rc(1,2,1).k(1).p(2).k(1).build()
		chartElementToOperationMap[CBL_1_2_1_LPC] = group(4).lc(1,2,1).k(1).p(2).k(1).build()
		chartElementToOperationMap[CBL_2_2_RT] = group(4).rc(2,2).k_tbl(2).k_tbl(2).build()
		chartElementToOperationMap[CBL_2_2_LT] = group(4).lc(2,2).k_tbl(2).k_tbl(2).build()

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
