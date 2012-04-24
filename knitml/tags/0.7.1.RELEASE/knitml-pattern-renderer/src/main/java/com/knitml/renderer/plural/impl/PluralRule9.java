package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule9 implements PluralRule {

	public String getDescription() {
		return "Slavic (Polish)";
	}

	public int getNumberOfForms() {
		return 3;
	}

	public int getPluralForm(double i) {
		if (i == 1) {
			return 0;
		} else if (i % 10 >= 2 && i % 10 <= 4
				&& (i % 100 < 10 || i % 100 >= 20)) {
			return 1;
		} else {
			return 2;
		}
	}

}
