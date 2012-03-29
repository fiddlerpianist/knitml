package com.knitml.renderer.chart.symbol.impl;

import java.util.EnumMap
import java.util.Map

import com.knitml.renderer.chart.ChartElement
import com.knitml.renderer.chart.symbol.SymbolResolutionException
import com.knitml.renderer.chart.symbol.Symbol
import com.knitml.renderer.chart.symbol.SymbolProvider

import static com.knitml.renderer.chart.ChartElement.*

class StitchMasterySymbolProvider implements SymbolProvider {

	public static final String DOT_ID = "Dot"
	public static final String DOT_CABLE_ID = "DotCable" // note: this is DotCableEH
	public static final String DASH_ID = "Dash"
	public static final String DASH_CABLE_ID = "DashCable" // note: this is DashCableEH

	protected Map<ChartElement, String> base = new EnumMap<ChartElement, String>(ChartElement)
	protected Map<ChartElement, String> cable = new EnumMap<ChartElement, String>(ChartElement)
	protected Mode mode = Mode.DOT

	public enum Mode { DASH, DOT };
	
	// Regexp find: (\S*?)\([1-9]\),\s*
	// Regexp replace: (\1):'',

	public StitchMasterySymbolProvider() {
		// basic stitches
		base << [(K):'k',(P):'p',(SL):'&',(SL_WYIF):'\'',(SL_KW):'&|',(SL_KW_WYIF):'\'|',(K_TW):'n',(P_TW):'?',(K_BELOW):'!',(P_BELOW):'%']
		// knit increases
		base << [(M1):'>',(M1R):';',(M1L):':',(KFB):'*',(KFBF):'=',(KPK_NEXT_ST):'*a',
					(INC1_TO_3):'*b',(INC1_TO_4):'*c',(INC1_TO_5):'*d',(INC1_TO_6):'*e',(INC1_TO_7):'*f',(INC1_TO_8):'*g',(INC1_TO_9):'*h']
		// purl increases
		base << [(M1P):'@',(M1RP):'y',(M1LP):'x',(PFB):'Q',(PFBF):')',(PKP_NEXT_ST):'Qa']
		// yarn-over increases
		base << [(YO):'o',(YO2):'A',(YO3):'oC',(YO4):'oD',(YO5):'oE',(YO6):'oF',(YO7):'oG',(YO8):'oH',(YO9):'oI',
					(KYK_NEXT_ST):'L',(PYP_NEXT_ST):'}']
		// knit decreases
		base << [(K2TOG):'/',(K2TOG_TBL):'q',(SSK):'\\',(SKP):'V',(K3TOG):'s',(SSSK):'t',(SK2P):'t',(CDD):'<',(S2K2P2):'04',
					(K4TOG):'04',(K5TOG):'05',(K6TOG):'06',(K7TOG):'07',(K8TOG):'08',(K9TOG):'09',
					(DEC4_TO_1):'04',(DEC5_TO_1):'05',(DEC6_TO_1):'06',(DEC7_TO_1):'07',(DEC8_TO_1):'08',(DEC9_TO_1):'09']
		// purl decreases
		base << [(P2TOG):'l',(P2TOG_TBL):'Z',(SSP):'m',(P3TOG):'u',(P2TOG_SWYIF_PSSO):'[',(CDDP):'(',(SWYIF_P2TOGTBL_PSSO):'v']
		// misc single width stitches
		base << [(DECREASE):'0', (NS):'w']

		// 2-st cables
		cable << [(CBL_1_1_RC):'\u0021',(CBL_1_1_LC):'\u0022',(CBL_1_1_RPC):'\u0023',(CBL_1_1_LPC):'\u0024',
					(CBL_1_1_RT):'\u0025',(CBL_1_1_LT):'\u0026',(CBL_1_1_RPT):'\u0029',(CBL_1_1_LPT):'\u002a',(CBL_2ST_CUSTOM):'\u00f1']
		// 3-st cables
		cable << [(CBL_2_1_RC):'\u002b',(CBL_2_1_LC):'\u002c',(CBL_1_2_RC):'\u002f',(CBL_1_2_LC):'\u0030',
					(CBL_2_1_RPC):'\u002d',(CBL_2_1_LPC):'\u002e',(CBL_1_2_RPC):'\u0031',(CBL_1_2_LPC):'\u0032',
					(CBL_1_1_1_RC):'\u0039',(CBL_1_1_1_LC):'\u003a',(CBL_1_1_1_RPC):'\u003b',(CBL_1_1_1_LPC):'\u003c',(CBL_1_1_1_RRC):'\u003d',(CBL_1_1_1_LRC):'\u003d',
					(CBL_2_1_RT):'\u0035',(CBL_2_1_LT):'\u0036',(CBL_2_1_RPT):'\u0037',(CBL_2_1_LPT):'\u0038',
					(CBL_1_1_1_RT_PURL):'\u00de',(CBL_1_1_1_LT_PURL):'\u00df',(CBL_3ST_CUSTOM):'\u00f2']
		// 4-st cables
		cable << [(CBL_2_2_RC):'\u003e',(CBL_2_2_LC):'\u003f',(CBL_2_2_RPC):'\u0040',(CBL_2_2_LPC):'\u0041',
					(CBL_1_3_RC):'\u0046',(CBL_1_3_LC):'\u0047',
					(CBL_1_3_RPC):'\u0048',(CBL_1_3_LPC):'\u0049',(CBL_3_1_RC):'\u004a',(CBL_3_1_LC):'\u004b',(CBL_3_1_RPC):'\u004c',(CBL_3_1_LPC):'\u004d',
					(CBL_1_2_1_RC):'\u004e',(CBL_1_2_1_LC):'\u004f',(CBL_1_2_1_RPC):'\u0050',(CBL_1_2_1_LPC):'\u0051',
					(CBL_2_2_RT):'\u0042',(CBL_2_2_LT):'\u0043',(CBL_4ST_CUSTOM):'\u00f3']
		// 5-st cables
		cable << [(CBL_2_3_RC):'\u0056',(CBL_2_3_LC):'\u0057',(CBL_2_3_RPC):'\u0058',(CBL_2_3_LPC):'\u0059',
					(CBL_3_2_RC):'\u005a',(CBL_3_2_LC):'\u005b',(CBL_3_2_RPC):'\\',(CBL_3_2_LPC):'\u005d',
					(CBL_4_1_RC):'\u005e',(CBL_4_1_LC):'\u005f',(CBL_4_1_RPC):'\u0060',(CBL_4_1_LPC):'\u0061',
					(CBL_1_4_RC):'\u0052',(CBL_1_4_LC):'\u0053',(CBL_1_4_RPC):'\u0054',(CBL_1_4_LPC):'\u0055',
					(CBL_2_1_2_RC):'\u0062',(CBL_2_1_2_LC):'\u0063',(CBL_2_1_2_RPC):'\u0064',(CBL_2_1_2_LPC):'\u0065',
					(CBL_1_3_1_RC):'\u0066',(CBL_1_3_1_LC):'\u0067',(CBL_1_3_1_RPC):'\u0068',(CBL_1_3_1_LPC):'\u0069',(CBL_5ST_CUSTOM):'\u00f4']
		// 6-st cables
		cable << [(CBL_3_3_RC):'\u006a',(CBL_3_3_LC):'\u006b',(CBL_3_3_RPC):'\u006c',(CBL_3_3_LPC):'\u006d',
					(CBL_2_4_RC):'\u006e',(CBL_2_4_LC):'\u006f',(CBL_2_4_RPC):'\u0070',(CBL_2_4_LPC):'\u0071',
					(CBL_4_2_RC):'\u0072',(CBL_4_2_LC):'\u0073',(CBL_4_2_RPC):'\u0074',(CBL_4_2_LPC):'\u0075',
					(CBL_2_2_2_RC):'\u0076',(CBL_2_2_2_LC):'\u0077',(CBL_2_2_2_RPC):'\u0078',(CBL_2_2_2_LPC):'\u0079',
					(CBL_2_2_2_RRC):'\u007a',(CBL_2_2_2_LRC):'\u007a',(CBL_6ST_CUSTOM):'\u00f5']
	}
	
	public StitchMasterySymbolProvider(Mode mode) {
		this();
		this.mode = mode;
	}

	public Symbol getSymbol(ChartElement element) throws SymbolResolutionException {
		def symbol = base[element]
		if (symbol != null) {
			return new Symbol(mode == Mode.DASH ? DASH_ID : DOT_ID, symbol)
		}
		symbol = cable[element]
		if (symbol != null) {
			return new Symbol(mode == Mode.DASH ? DASH_CABLE_ID : DOT_CABLE_ID, symbol)
		}
		throw new SymbolResolutionException(this, element)
	}

}
