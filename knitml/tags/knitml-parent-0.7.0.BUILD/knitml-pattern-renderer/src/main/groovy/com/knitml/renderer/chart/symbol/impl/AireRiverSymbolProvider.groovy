package com.knitml.renderer.chart.symbol.impl

import static com.knitml.renderer.chart.ChartElement.*

public class AireRiverSymbolProvider extends
AbstractSingleSymbolSetProvider {

	@Override
	protected void initializeSymbols() {
		// basic stitches
		symbols << [(K):' ',(P):'/',(SL):',',(SL_WYIF):'.',(SL_KW):'u',(SL_KW_WYIF):'.',(K_TW):'\u00f2',(P_TW):'\u00f3',(K_BELOW):'b',(P_BELOW):'n']
		// knit increases
		symbols << [(M1):'k',(M1R):'l',(M1L):';',(KFB):'o',
					(INC1_TO_3):'8',(INC1_TO_4):'9',(INC1_TO_5):'0']
		// purl increases
		symbols << [(M1P):']',(M1RP):'l',(M1LP):';',(PFB):'i']
		// yarn-over increases
		symbols << [(YO):'j',(YO2):'\u00ec',(YO3):'\u00ed',(YO4):'\u00ee',(YO5):'\u00ef']
		// knit decreases
		symbols << [(K2TOG):'|',(K2TOG_TBL):'x',(SSK):'\\',(SKP):'s',(K3TOG):'f',(SSSK):'a',(SK2P):'a',(CDD):'v',(S2K2P2):'4',
					(K4TOG):'4',(K5TOG):'5',
					(DEC4_TO_1):'4',(DEC5_TO_1):'5']
		// purl decreases
		symbols << [(P2TOG):'e',(P2TOG_TBL):'w',(SSP):'c',(P3TOG):'r',(P2TOG_SWYIF_PSSO):'2',(CDDP):'v',(SWYIF_P2TOGTBL_PSSO):'2']
		// misc single width stitches
		symbols << [(DECREASE):'1', (NS):'[']

		// 2-st cables
		symbols << [(CBL_1_1_RC):'YU',(CBL_1_1_LC):'RT',(CBL_1_1_RPC):'OU',(CBL_1_1_LPC):'RW',
					(CBL_1_1_RT):'Y&',(CBL_1_1_LT):'$T',(CBL_1_1_RPT):'O&',(CBL_1_1_LPT):'$W',(CBL_2ST_CUSTOM):'**']
		// 3-st cables
		symbols << [(CBL_2_1_RC):'YUE',(CBL_2_1_LC):'ERT',(CBL_1_2_RC):'IYU',(CBL_1_2_LC):'RTI',
					(CBL_2_1_RPC):'OUE',(CBL_2_1_LPC):'ERW',(CBL_1_2_RPC):'}OU',(CBL_1_2_LPC):'RW}',
					//(CBL_1_1_1_RC):'',(CBL_1_1_1_LC):'',(CBL_1_1_1_RPC):'',(CBL_1_1_1_LPC):'',(CBL_1_1_1_RRC):'',(CBL_1_1_1_LRC):'',
					(CBL_2_1_RT):'Y&#',(CBL_2_1_LT):'#$T',(CBL_2_1_RPT):'O&#',(CBL_2_1_LPT):'#$W',
					//(CBL_1_1_1_RT_PURL):'',(CBL_1_1_1_LT_PURL):'',
					(CBL_3ST_CUSTOM):'***']
		// 4-st cables
		symbols << [(CBL_2_2_RC):'IYUE',(CBL_2_2_LC):'ERTI',(CBL_2_2_RPC):'}OUE',(CBL_2_2_LPC):'ERW}',
					(CBL_1_3_RC):'IIYU',(CBL_1_3_LC):'RTII',(CBL_1_3_RPC):'}}OU',(CBL_1_3_LPC):'QTII',
					(CBL_3_1_RC):'YUEE',(CBL_3_1_LC):'EERT',(CBL_3_1_RPC):'OUEE',(CBL_3_1_LPC):'EERW',
//					(CBL_1_2_1_RC):'',(CBL_1_2_1_LC):'',(CBL_1_2_1_RPC):'',(CBL_1_2_1_LPC):'',
					(CBL_2_2_RT):'IY&#',(CBL_2_2_LT):'#$TI',(CBL_4ST_CUSTOM):'****']
		// 5-st cables
		symbols << [(CBL_2_3_RC):'IIYUE',(CBL_2_3_LC):'ERTII',(CBL_2_3_RPC):'}}OUE',(CBL_2_3_LPC):'ERW}}',
					(CBL_3_2_RC):'IYUEE',(CBL_3_2_LC):'EERTI',(CBL_3_2_RPC):'}OUEE',(CBL_3_2_LPC):'EERW}',
					(CBL_4_1_RC):'YUEEE',(CBL_4_1_LC):'EEERT',(CBL_4_1_RPC):'OUEEE',(CBL_4_1_LPC):'EEERW',
					(CBL_1_4_RC):'IIIYU',(CBL_1_4_LC):'RTIII',(CBL_1_4_RPC):'}}}OU',(CBL_1_4_LPC):'RW}}}',
//					(CBL_2_1_2_RC):'',(CBL_2_1_2_LC):'',(CBL_2_1_2_RPC):'',(CBL_2_1_2_LPC):'',
//					(CBL_1_3_1_RC):'',(CBL_1_3_1_LC):'',(CBL_1_3_1_RPC):'',(CBL_1_3_1_LPC):'',
					(CBL_5ST_CUSTOM):'*****']
		// 6-st cables
		symbols << [(CBL_3_3_RC):'IIYUEE',(CBL_3_3_LC):'EERTII',(CBL_3_3_RPC):'}}OUEE',(CBL_3_3_LPC):'EERW}}',
					(CBL_2_4_RC):'IIIYUE',(CBL_2_4_LC):'ERTIII',(CBL_2_4_RPC):'}}}OUE',(CBL_2_4_LPC):'ERW}}}',
					(CBL_4_2_RC):'IYUEEE',(CBL_4_2_LC):'EEERTI',(CBL_4_2_RPC):'}OUEEE',(CBL_4_2_LPC):'EEERW}',
//					(CBL_2_2_2_RC):'',(CBL_2_2_2_LC):'',(CBL_2_2_2_RPC):'',(CBL_2_2_2_LPC):'',
//					(CBL_2_2_2_RRC):'',(CBL_2_2_2_LRC):'',
					(CBL_6ST_CUSTOM):'******']
	}
}
