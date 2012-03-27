package com.knitml.renderer.chart.symbol.impl;

import java.util.EnumMap
import java.util.Map

import com.knitml.renderer.chart.ChartElement
import com.knitml.renderer.chart.symbol.SymbolResolutionException
import com.knitml.renderer.chart.symbol.Symbol
import com.knitml.renderer.chart.symbol.SymbolProvider

import static com.knitml.renderer.chart.ChartElement.*

class StitchMasterySymbolProvider implements SymbolProvider {

	public static final String DOT = "Dot"
	public static final String DOT_CABLE = "DotCable"
	public static final String DASH = "Dash"
	public static final String DASH_CABLE = "DashCable"

	protected Map<ChartElement, String> dot = new EnumMap<ChartElement, String>(ChartElement)
	protected Map<ChartElement, String> dotCable = new EnumMap<ChartElement, String>(ChartElement)

	// Regexp find: (\S*?)\([1-9]\),\s*
	// Regexp replace: (\1):'',

	public StitchMasterySymbolProvider() {
		// basic stitches
		dot << [(K):'k',(P):'p',(SL):'&',(SL_WYIF):'\'',(K_TW):'n',(P_TW):'?',(K_BELOW):'!',(P_BELOW):'%']
		// knit increases
		dot << [(M1):'>',(M1R):';',(M1L):':',(KFB):'*',(KFBF):'=',(KPK_NEXT_ST):'*a',
					(INC1_TO_3):'*b',(INC1_TO_4):'*c',(INC1_TO_5):'*d',(INC1_TO_6):'*e',(INC1_TO_7):'*f',(INC1_TO_8):'*g',(INC1_TO_9):'*h']
		// purl increases
		dot << [(M1P):'@',(M1RP):'y',(M1LP):'x',(PFB):'Q',(PFBF):')',(PKP_NEXT_ST):'Qa']
		// yarn-over increases
		dot << [(YO):'o',(YO2):'A',(YO3):'oC',(YO4):'oD',(YO5):'oE',(YO6):'oF',(YO7):'oG',(YO8):'oH',(YO9):'oI',
					(KYK_NEXT_ST):'L',(PYP_NEXT_ST):'}']
		// knit decreases
		dot << [(K2TOG):'/',(K2TOG_TBL):'q',(SSK):'\\',(SKP):'V',(K3TOG):'s',(SSSK):'t',(SK2P):'t',(CDD):'<',(S2K2P2):'04',
					(K4TOG):'04',(K5TOG):'05',(K6TOG):'06',(K7TOG):'07',(K8TOG):'08',(K9TOG):'09',
					(DEC4_TO_1):'04',(DEC5_TO_1):'05',(DEC6_TO_1):'06',(DEC7_TO_1):'07',(DEC8_TO_1):'08',(DEC9_TO_1):'09']
		// purl decreases
		dot << [(P2TOG):'l',(P2TOG_TBL):'Z',(SSP):'m',(P3TOG):'u',(P2TOG_SWYIF_PSSO):'[',(CDDP):'(',(SWYIF_P2TOGTBL_PSSO):'v']
		// misc single width stitches
		dot << [(DECREASE):'0', (NS):'w']

		dotCable << [(CBL_2_2_RC):'\u003e',(CBL_2_2_LC):'\u003f']
	}

	public Symbol getSymbol(ChartElement element) throws SymbolResolutionException {
		def symbol = dot[element]
		if (symbol != null) {
			return new Symbol(DOT, symbol)
		}
		symbol = dotCable[element]
		if (symbol != null) {
			return new Symbol(DOT_CABLE, symbol)
		}
		throw new SymbolResolutionException(this, element)
	}

}
