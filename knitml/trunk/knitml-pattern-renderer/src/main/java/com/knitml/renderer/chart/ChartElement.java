package com.knitml.renderer.chart;

public enum ChartElement {

	// basic elements
	K(1), P(1), SL(1), SL_WYIF(1), SL_KW(1), SL_KW_WYIF(1), K_TW(1), P_TW(1), K_BELOW(1), P_BELOW(1),
	// knit increases
	M1(1), M1R(1), M1L(1), KFB(1), KFBF(1), KPK_NEXT_ST(1), INC1_TO_3(1), INC1_TO_4(
			1), INC1_TO_5(1), INC1_TO_6(1), INC1_TO_7(1), INC1_TO_8(1), INC1_TO_9(
			1),
	// purl increases
	M1P(1), M1RP(1), M1LP(1), PFB(1), PFBF(1), PKP_NEXT_ST(1),
	// yarn-over increases
	YO(1), YO2(1), YO3(1), YO4(1), YO5(1), YO6(1), YO7(1), YO8(1), YO9(1), KYK_NEXT_ST(
			1), PYP_NEXT_ST(1),
	// knit decreases
	K2TOG(1), K2TOG_TBL(1), SSK(1), SKP(1), K3TOG(1), SSSK(1), SK2P(1), CDD(1), S2K2P2(
			1), K4TOG(1), K5TOG(1), K6TOG(1), K7TOG(1), K8TOG(1), K9TOG(1), DEC4_TO_1(
			1), DEC5_TO_1(1), DEC6_TO_1(1), DEC7_TO_1(1), DEC8_TO_1(1), DEC9_TO_1(
			1),
	// purl decreases
	P2TOG(1), P2TOG_TBL(1), SSP(1), P3TOG(1), P2TOG_SWYIF_PSSO(1), CDDP(1), SWYIF_P2TOGTBL_PSSO(
			1),
	// misc single width stitches
	DECREASE(1), NS(1),
	// 2-st cables
	CBL_1_1_RC(2), CBL_1_1_LC(2), CBL_1_1_RPC(2), CBL_1_1_LPC(2), CBL_1_1_RT(2), CBL_1_1_LT(
			2), CBL_1_1_RPT(2), CBL_1_1_LPT(2), CBL_2ST_CUSTOM(2),
	// 3-st cables
	CBL_2_1_RC(3), CBL_2_1_LC(3), CBL_1_2_RC(3), CBL_1_2_LC(3), CBL_2_1_RPC(3), CBL_2_1_LPC(
			3), CBL_1_2_RPC(3), CBL_1_2_LPC(3), CBL_1_1_1_RC(3), CBL_1_1_1_LC(3), CBL_1_1_1_RPC(
			3), CBL_1_1_1_LPC(3), CBL_1_1_1_RRC(3), CBL_1_1_1_LRC(3), CBL_2_1_RT(
			3), CBL_2_1_LT(3), CBL_2_1_RPT(3), CBL_2_1_LPT(3), CBL_1_1_1_RT_PURL(
			3), CBL_1_1_1_LT_PURL(3), CBL_3ST_CUSTOM(3),
	// 4-st cables
	CBL_2_2_RC(4), CBL_2_2_LC(4), CBL_2_2_RPC(4), CBL_2_2_LPC(4), CBL_1_3_RC(4), CBL_1_3_LC(
			4), CBL_1_3_RPC(4), CBL_1_3_LPC(4), CBL_3_1_RC(4), CBL_3_1_LC(4), CBL_3_1_RPC(
			4), CBL_3_1_LPC(4), CBL_1_2_1_RC(4), CBL_1_2_1_LC(4), CBL_1_2_1_RPC(
			4), CBL_1_2_1_LPC(4), CBL_2_2_RT(4), CBL_2_2_LT(4), CBL_4ST_CUSTOM(
			4);

	private int width;

	ChartElement(int width) {
		this.width = width;
	}

	public int width() {
		return width;
	}
}
