package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule4 implements PluralRule {

	public String getDescription() {
		return "Celtic (Scottish Gaelic)";
	}

	public int getNumberOfForms() {
		return 3;
	}
	
	public int getPluralForm(double i) {
		if (i == 1) {
			return 0;
		} else if (i == 2) {
			return 1;
		} else {
			return 2;
		}
	}

}
