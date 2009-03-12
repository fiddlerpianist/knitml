package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule1 implements PluralRule {

	public String getDescription() {
		return "Germanic (Danish, Dutch, English, Faroese, Frisian, German, Norwegian, Swedish), Finno-Ugric (Estonian, Finnish, Hungarian), Language isolate (Basque), Latin/Greek (Greek), Semitic (Hebrew), Romanic (Italian, Portuguese, Spanish, Catalan)";
	}

	public int getNumberOfForms() {
		return 2;
	}
	
	public int getPluralForm(double numberToExamine) {
		if (numberToExamine == 1) {
			return 0;
		} else {
			return 1;
		}
	}

}
