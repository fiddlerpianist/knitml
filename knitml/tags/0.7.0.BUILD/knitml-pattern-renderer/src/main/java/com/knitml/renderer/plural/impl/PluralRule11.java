package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule11 implements PluralRule {

	public String getDescription() {
		return "Celtic (Irish Gaeilge)";
	}

	public int getNumberOfForms() {
		return 5;
	}
	
	public int getPluralForm(double i) {
		if (i == 1) {
			return 0;
		} else if (i == 2) {
			return 1;
		} else if (i >= 3 && i <= 6){
			return 2;
		} else if (i >= 7 && i <= 10){
			return 3;
		} else {
			return 4;
		}
	}

}
