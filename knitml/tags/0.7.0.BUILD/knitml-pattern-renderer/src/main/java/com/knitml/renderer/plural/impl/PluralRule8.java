package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule8 implements PluralRule {

	public String getDescription() {
		return "Slavic (Slovak, Czech)";
	}

	public int getNumberOfForms() {
		return 3;
	}

	public int getPluralForm(double i) {
		if (i == 1) {
			return 0;
		} else if (i >= 2 && i <= 4) {
			return 1;
		} else {
			return 2;
		}
	}

}
