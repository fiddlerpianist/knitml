package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule13 implements PluralRule {

	public String getDescription() {
		return "Semitic (Maltese)";
	}

	public int getNumberOfForms() {
		return 4;
	}

	public int getPluralForm(double i) {
		if (i == 1) {
			return 0;
		} else if (i == 0 || i % 100 > 0 && i % 100 <= 10) {
			return 1;
		} else if (i % 100 > 10 && i % 100 < 20) {
			return 2;
		} else {
			return 3;
		}
	}

}
