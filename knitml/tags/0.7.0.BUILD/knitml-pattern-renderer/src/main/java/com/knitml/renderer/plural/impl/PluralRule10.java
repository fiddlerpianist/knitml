package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule10 implements PluralRule {

	public String getDescription() {
		return "Slavic (Slovenian, Sorbian)";
	}

	public int getNumberOfForms() {
		return 4;
	}

	public int getPluralForm(double i) {
		if (i % 100 == 1) {
			return 0;
		} else if (i % 100 == 2) {
			return 1;
		} else if ((i % 100 == 3) || (i % 100 == 4)) {
			return 2;
		} else {
			return 3;
		}
	}

}
