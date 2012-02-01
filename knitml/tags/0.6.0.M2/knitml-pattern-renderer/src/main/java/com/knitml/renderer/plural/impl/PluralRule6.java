package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule6 implements PluralRule {

	public String getDescription() {
		return "Baltic (Lithuanian)";
	}

	public int getNumberOfForms() {
		return 3;
	}

	public int getPluralForm(double i) {
		if (i % 10 == 1 && i % 100 != 11) {
			return 0;
		} else if (i % 10 >= 2 && (i % 100 < 10 || i % 100 >= 20)) {
			return 2;
		} else {
			return 1;
		}
	}

}
