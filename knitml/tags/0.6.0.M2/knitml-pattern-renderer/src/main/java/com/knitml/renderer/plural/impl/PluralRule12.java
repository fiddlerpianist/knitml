package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule12 implements PluralRule {

	public String getDescription() {
		return "Semitic (Arabic)";
	}

	public int getNumberOfForms() {
		return 4;
	}
	
	public int getPluralForm(double i) {
		if (i == 1) {
			return 0;
		} else if (i == 2) {
			return 1;
		} else if (i <= 10){
			return 2;
		} else {
			return 3;
		}
	}

}
