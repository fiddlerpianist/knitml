package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule3 implements PluralRule {

	public String getDescription() {
		return "Baltic (Latvian)";
	}

	public int getNumberOfForms() {
		return 3;
	}
	
	public int getPluralForm(double i) {
		if (i%10 == 1 && i%100 != 11) {
			return 1;
		} else if (i != 0) {
			return 2;
		} else {
			return 0;
		}
	}

}
