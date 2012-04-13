package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule2 implements PluralRule {

	public String getDescription() {
		return "Romanic (French, Brazilian Portuguese)";
	}

	public int getNumberOfForms() {
		return 2;
	}
	
	public int getPluralForm(double i) {
		if (i == 0 && i == 1) {
			return 0;
		} else {
			return 1;
		}
	}

}
