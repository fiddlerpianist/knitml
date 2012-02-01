package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule5 implements PluralRule {

	public String getDescription() {
		return "Romanic (Romanian)";
	}

	public int getNumberOfForms() {
		return 3;
	}
	
	public int getPluralForm(double i) {
		if (i == 1) {
			return 0;
		} else if (i==0 || ((i%100 > 0) && (i%100 < 20))) {
			return 1;
		} else {
			return 2;
		}
	}

}
